package com.invest.honduras.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.everis.blockchain.honduras.util.HashUtils;
import com.invest.honduras.blockchain.StepManagerBlockChain;
import com.invest.honduras.config.ConfigurationProject;
import com.invest.honduras.dao.ProcessDao;
import com.invest.honduras.dao.ProjectDao;
import com.invest.honduras.dao.UserDao;
import com.invest.honduras.domain.map.ProcessMap;
import com.invest.honduras.domain.model.Project;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.process.Process;
import com.invest.honduras.enums.TypeStatusCode;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.http.client.ProcessInitRequest;
import com.invest.honduras.http.client.process.ApproveRequest;
import com.invest.honduras.http.client.process.CommitteeDeleteRequest;
import com.invest.honduras.http.client.process.CommitteeRequest;
import com.invest.honduras.http.client.process.CommitteeUpdateRequest;
import com.invest.honduras.http.client.process.ProcessRequest;
import com.invest.honduras.http.response.process.ProcessItemResponse;
import com.invest.honduras.notify.NotifyClient;
import com.invest.honduras.notify.request.ItemUserNotifyRequest;
import com.invest.honduras.service.ProcessService;
import com.invest.honduras.util.Constant;
import com.invest.honduras.util.Constants;
import com.invest.honduras.util.FlowConstants;
import com.invest.honduras.util.FlowUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProcessServiceImpl implements ProcessService {

	@Autowired
	ProcessDao processDao;

	@Autowired
	NotifyClient notifyClient;

	@Autowired
	UserDao userDao;

	@Autowired
	ProjectDao projectDao;

	@Autowired
	StepManagerBlockChain blockChain;

	@Autowired
	ConfigurationProject config;

	@Override
	public Mono<Boolean> existsDocument(String idProject, String idProcess, String typeDocument, String hash) {
		return projectDao.findById(idProject).flatMap(project -> {

			return blockChain.getDocument(project.getIdAccredited(), typeDocument, hash).flatMap(doc -> {
				return Mono.just(Objects.equals( hash, doc.getHash()));
			});

		});
	}
	
	@Override
	public Mono<Boolean> existsComment(String idProject, String idProcess, String typeDocument, String hash) {
		return projectDao.findById(idProject).flatMap(project -> {

			return blockChain.getComment(project.getIdAccredited(), typeDocument, hash).flatMap(doc -> {
				return Mono.just(Objects.equals( hash, doc.getHash()));
			});

		});
	}

	@Override
	public Mono<ProcessItemResponse> findByIdProject(String idProject) {
		return processDao.findByIdProject(idProject).map(item -> {
			ProcessItemResponse destiny = new ProcessItemResponse();
			new ProcessMap().mapping(item, destiny);
			return destiny;
		});
	}

	@Override
	public Mono<ProcessItemResponse> findById(String id) {
		return processDao.findById(id).map(item -> {
			ProcessItemResponse destiny = new ProcessItemResponse();
			new ProcessMap().mapping(item, destiny);
			return destiny;
		});
	}

	@Override
	public Mono<ProcessItemResponse> updateProcess(String id, ProcessRequest request, UserSession userSession) {

		return userDao.findByEmail(userSession.getEmail()).flatMap(user -> {

			return projectDao.findById(request.getIdProject()).flatMap(project -> {

				return blockChain.hasRoleUser(userSession.getRole(), user.getProxyAddress()).flatMap(flag -> {

					if (flag) {
						return blockChain.setChangeStepBlockchain(project.getIdAccredited(), user.getProxyAddress(),
								userSession.getRole(), request.getCodeStep()).flatMap(response -> {
									if (HttpStatus.OK.value() == response.getStatus()) {

										return saveUpdateProcess(project, id, request, userSession);
									} else {
										throw new GlobalException(HttpStatus.BAD_REQUEST, response.getError());
									}

								});
					} else {
						throw new GlobalException(HttpStatus.BAD_REQUEST,
								TypeStatusCode.MESSAGE_USER_NOT_ROLE.name() + ":" + userSession.getRole());
					}
				});

			});

		});

	}


	private Mono<ProcessItemResponse> saveUpdateProcess(Project project, String id, ProcessRequest request,
			UserSession userSession) {
		return processDao.updateProcess(id, request, userSession).flatMap(item -> {

			updateProject(item, userSession);

			ProcessItemResponse destiny = new ProcessItemResponse();

			new ProcessMap().mapping(item, destiny);

			sendNotifyEmailUpdate(project, item, request.getCodeStep());

			return Mono.just(destiny);
		});
	}

	@Override
	public Mono<ProcessItemResponse> initProcess(String idProject, ProcessInitRequest request,
			UserSession userSession) {
		return userDao.findByEmail(userSession.getEmail()).flatMap(user -> {

			return projectDao.findById(idProject).flatMap(project -> {
				return processDao.initProcess(project, request.getCodeStep(), userSession.getRole(), user)
						.map(process -> {
							updateProject(process, userSession);
							ProcessItemResponse destiny = new ProcessItemResponse();
							new ProcessMap().mapping(process, destiny);
							return destiny;
						});
			});

		});
	}

	private void sendNotifyEmailUpdate(Project project, Process process, String codeStep) {
		String observation = "";
		String nameProcess = project.getSolicitude().getDataProcess().getProcessName();

		List<String> listRoles = new ArrayList<>();

		String url="";
		switch (codeStep) {
		/* ADD RDELAROSA 06/05/2020 */
		case FlowConstants.STEP_START_PROCESS:
			url = String.format(Constant.URL_PROCESS_STEP_2,  project.getId());
			observation = String.format(Constant.NOTIFY_STEP_INIT_PROCCESS, nameProcess);
			listRoles.add(Constants.ROLE_ESP_ADQ);
			break;
		/****************************/

		case FlowConstants.STEP_OFFER_DOCUMENTATION:
			url = String.format(Constant.URL_PROCESS_STEP_3,  project.getId());

			observation = String.format(Constant.NOTIFY_STEP_OFFER_DOCUMENTATION, nameProcess);

			listRoles.add(Constants.ROLE_DIR_ADJ);
			break;

		case FlowConstants.STEP_COMMITEE:
			url = String.format(Constant.URL_PROCESS_STEP_4,  project.getId());

			observation = String.format(Constant.NOTIFY_STEP_COMMITEE, nameProcess);

			listRoles.add(Constants.ROLE_ESP_ADQ);
			break;

		case FlowConstants.STEP_EVALUATION_REPORT:
			url = String.format(Constant.URL_PROCESS_STEP_5,  project.getId());

			observation = String.format(Constant.NOTIFY_STEP_EVALUATION_REPORT, nameProcess);

			listRoles.add(Constants.ROLE_DIR_ADM_FIN);

			break;

		case FlowConstants.STEP_STRUCTURE_BUDGET:
			url = String.format(Constant.URL_PROCESS_STEP_6,  project.getId());

			log.info("CUANDO DAS GUARDAR Y CONTINUAR AL PASO 5...");

			observation = String.format(Constant.NOTIFY_STEP_STRUCTURE_BUDGET, nameProcess);

			log.info("observacion PASO 5: " + observation);

			listRoles.add(Constants.ROLE_ESP_ADQ);

			break;

		case FlowConstants.STEP_AWARD_RESOLUTION:
			url = String.format(Constant.URL_PROCESS_STEP_7,  project.getId());

			log.info("CUANDO DAS GUARDAR Y CONTINUAR AL PASO 6....");

			observation = String.format(Constant.NOTIFY_STEP_AWARD_RESOLUTION, nameProcess);

			log.info("observacion PASO 6: " + observation);

			listRoles.add(Constants.ROLE_ESP_ADQ);

			break;

		case FlowConstants.STEP_SIGNED_CONTRACT:
			url = Constant.URL_HOME_PROCESS;


			log.info("CUANDO DAS GUARDAR Y CONTINUAR AL PASO 7....");

			observation = String.format(Constant.NOTIFY_STEP_SIGNED_CONTRACT, nameProcess);

			log.info("observacion: " + observation);

			listRoles.add(Constants.ROLE_COO_TEC);

			break;

		default:
			break;
		}
		if (!StringUtils.isEmpty(observation))
			sendEmail(listRoles, observation, url);
	}

	private void sendEmail(List<String> listRoles, String mesage, String relativePath) {
		userDao.findUserByRoles(listRoles).collectList().subscribe(list -> {

			list.forEach(user -> {

				log.info("codeStatus ..." + user.getCodeStatus());
				if (user.getCodeStatus().equals(Constant.USER_HABILITADO)) {
					if (!StringUtils.isEmpty(user.getEmail())) {

						log.info("sendEmailAccredited ..." + user.getEmail());

						ItemUserNotifyRequest request = new ItemUserNotifyRequest();
						request.setEmail(user.getEmail());
							request.setUserId(user.getId());
							request.setName(user.getFullname());
	
							request.setObs(mesage);
							request.setTemplate(Constant.TYPE_TEMPLATE);
							request.setSubject(Constant.NOTIFY_SUBJET);
							
							request.setDetailsURL(config.getConfiguration().getUrlLogin()+relativePath);
	
							notifyClient.sendUpdateNotify(request);
						}
					}
				});
	
			});
		}
	
		@Override
		public Mono<ProcessItemResponse> addUserCommittee(String idProcess, CommitteeRequest request,
				UserSession userSession) {
	
			return processDao.addProcessCommittee(idProcess, request, userSession).flatMap(item -> {
	
				updateProject(item, userSession);
				ProcessItemResponse destiny = new ProcessItemResponse();
			new ProcessMap().mapping(item, destiny);

			// sendNotifyEmail(item);

			return Mono.just(destiny);
		});
	}

	@Override
	public Mono<ProcessItemResponse> updateUserCommittee(String idProcess, CommitteeUpdateRequest request,
			UserSession userSession) {

		return processDao.updateProcessCommittee(idProcess, request, userSession).flatMap(item -> {

			updateProject(item, userSession);
			ProcessItemResponse destiny = new ProcessItemResponse();
			new ProcessMap().mapping(item, destiny);

			// sendNotifyEmail(item);

			return Mono.just(destiny);
		});
	}

	@Override
	public Mono<ProcessItemResponse> deleteUserCommittee(CommitteeDeleteRequest request, UserSession userSession) {

		return processDao.deleteProcessCommittee(request.getIdProcess(), request.getIdCommittee(), userSession)
				.flatMap(item -> {

					updateProject(item, userSession);
					ProcessItemResponse destiny = new ProcessItemResponse();
					new ProcessMap().mapping(item, destiny);

					// sendNotifyEmail(item);

					return Mono.just(destiny);
				});
	}

	@Override
	public Mono<ProcessItemResponse> approveDocument(String idProcess, ApproveRequest request, UserSession userSession,
			boolean flag) {

		log.info("PRoccessServiceImpl : Entro a approaveDocument.....");

		String hashComment = HashUtils.convetStringToHashKeccakHex(request.getObservation().getBytes());

		return blockChain.approveDocumentBlockchain(request, idProcess, userSession, hashComment).flatMap(response -> {

			log.info("PRoccessServiceImpl : luego de aproveDocumentBLockChain.....");

			if (HttpStatus.OK.value() == response.getStatus()) {

				log.info("PRoccessServiceImpl : antes de saveAprroveDocument.....");

				return saveApproveDocument(idProcess, request, userSession, hashComment, flag);

			} else {
				throw new GlobalException(HttpStatus.OK, response.getError());
			}
		});
	}

	private Mono<ProcessItemResponse> saveApproveDocument(String idProcess, ApproveRequest request,
			UserSession userSession, String hash, boolean flag) {
		return userDao.findByEmail(userSession.getEmail()).flatMap(user -> {

			log.info("ProccessServiceImpl : + saveApproveDOcument antes del DAO UpdatePRoccessApproved");

			return processDao.updateProcessApproved(idProcess, hash, request, user, flag).flatMap(item -> {

				ProcessItemResponse destiny = new ProcessItemResponse();

				new ProcessMap().mapping(item, destiny);

				updateProject(item, userSession);

				sendEmailApprove(item, request);

				return Mono.just(destiny);
			});
		});
	}

	private void sendEmailApprove(Process process, ApproveRequest request) {
		String documentDescription = FlowUtil.getDescriptionByCodeDocument(config.getRule(), request.getDocumentType());

		String observation = String.format(Constant.NOTIFY_DOC_APPROVE_GENERAL, documentDescription,
				request.getNameFile());

		log.info("NRO APRROVED:" + request.getNroApproved());

		log.info("TIPO DE APROBACION:" + request.getType());

		List<String> listRoles = new ArrayList<>();

		String url = "";
		switch (request.getCodeStep()) {
		case FlowConstants.STEP_START_PROCESS:
			url = String.format(Constant.URL_PROCESS_STEP_1, process.getIdProject());
			if (FlowConstants.DOC_BIDDING_ATTACH.equals(request.getDocumentType())) {

				if (request.getType().compareTo(Constant.TYPE_REVIEW_OBSERVED) == 0) {

					log.info("DOCUMENTO DE LICITACION - LUEGO DE QUE SE OBSERVO");

					observation = String.format(Constant.NOTIFY_DOC_OBSERVED_GENERAL, documentDescription,
							request.getNameFile());
					listRoles.add(Constants.ROLE_ESP_ADQ);

				} else {

					/*********** ADD RDELAROSA 06052020 **********/
					if (request.getType().compareTo(FlowConstants.DOC_BIDDING_ATTACH_ACCREDIT) == 0) {

						log.info("DOCUMENTO DE LICITACION - LUEGO DE A ACREDITAR");

						observation = String.format(Constant.NOTIFY_DOC_BIDDING_FINALLY, documentDescription,
								request.getNameFile());
						listRoles.add(Constants.ROLE_ESP_ADQ);

					} else {

						if (request.getNroApproved().compareTo(FlowConstants.DOC_BIDDING_ATTACH_NUMBER_APPROVED) == 0)

						{
							log.info("DOCUMENTO DE LICITACION - LUEGO DE TODAS LAS APROBACIONES");

							observation = String.format(Constant.NOTIFY_DOC_BIDDING_ACREDITED, documentDescription,
									request.getNameFile());
							listRoles.add(Constants.ROLE_DIR_EJE);
						} else {

							log.info("DOCUMENTO DE LICITACION - LUEGO DE ADJUNTAR O APROBAR");

							listRoles.add(Constants.ROLE_DIR_ADQ);
							listRoles.add(Constants.ROLE_DIR_ADJ);
						}

					}

					/*****************************************/

				}

				break;
			}

			if (FlowConstants.DOC_CLARIFICATION_RESPONSE.equals(request.getDocumentType())) {

				if (request.getType().compareTo(Constant.TYPE_REVIEW_OBSERVED) == 0) {

					log.info("ACLARATORIA - LUEGO DE QUE SE OBSERVO");

					observation = String.format(Constant.NOTIFY_DOC_OBSERVED_GENERAL, documentDescription,
							request.getNameFile());
					listRoles.add(Constants.ROLE_ESP_ADQ);

				} else {

					/*********** ADD RDELAROSA 06052020 **********/
					if (request.getType().compareTo(FlowConstants.DOC_CLARIFICATION_RESPONSE_ACCREDIT) == 0) {

						observation = String.format(Constant.NOTIFY_CLARIFICATION_FINALLY, documentDescription,
								request.getNameFile());
						listRoles.add(Constants.ROLE_ESP_ADQ);

					} else {

						if (request.getNroApproved()
								.compareTo(FlowConstants.DOC_CLARIFICATION_RESPONSE_NUMBER_APPROVED) == 0) {

							observation = String.format(Constant.NOTIFY_CLARIFICATION_ACREDITED, documentDescription,
									request.getNameFile());
							listRoles.add(Constants.ROLE_DIR_EJE);
						} else {

							listRoles.add(Constants.ROLE_COO_TEC);
							listRoles.add(Constants.ROLE_DIR_ADQ);
							listRoles.add(Constants.ROLE_DIR_ADJ);
						}

					}

					/*****************************************/

				}

				break;
			}

			if (FlowConstants.DOC_AMENDMENT_RESPONSE.equals(request.getDocumentType())) {

				if (request.getType().compareTo(Constant.TYPE_REVIEW_OBSERVED) == 0) {

					log.info("ENMIENDA - LUEGO DE QUE SE OBSERVO");

					observation = String.format(Constant.NOTIFY_DOC_OBSERVED_GENERAL, documentDescription,
							request.getNameFile());
					listRoles.add(Constants.ROLE_ESP_ADQ);

				} else {

					/*********** ADD RDELAROSA 06052020 **********/
					if (request.getType().compareTo(FlowConstants.DOC_AMENDMENT_RESPONSE_ACCREDIT) == 0) {

						observation = String.format(Constant.NOTIFY_AMENDMENT_FINALLY, documentDescription,
								request.getNameFile());
						listRoles.add(Constants.ROLE_ESP_ADQ);

					} else {

						if (request.getNroApproved()
								.compareTo(FlowConstants.DOC_AMENDMENT_RESPONSE_NUMBER_APPROVED) == 0) {

							observation = String.format(Constant.NOTIFY_AMENDMENT_ACREDITED, documentDescription,
									request.getNameFile());
							listRoles.add(Constants.ROLE_DIR_EJE);
						} else {

							listRoles.add(Constants.ROLE_COO_TEC);
							listRoles.add(Constants.ROLE_DIR_ADQ);
							listRoles.add(Constants.ROLE_DIR_ADJ);
						}

					}

					/*****************************************/

				}

				break;
			}

			break;

		/* ADD RDELAROSA 15/05/2020 */
		case FlowConstants.STEP_EVALUATION_REPORT:
			url = String.format(Constant.URL_PROCESS_STEP_4, process.getIdProject());

			/* Agregar FLujo de Aprobacion de Informe de Evaluacion */
			if (FlowConstants.DOC_EVALUATION_REPORT.equals(request.getDocumentType())) {

				if (request.getType().compareTo(Constant.TYPE_REVIEW_OBSERVED) == 0) {

					log.info("Informe de Evaluacion - LUEGO DE QUE SE OBSERVO");

					observation = String.format(Constant.NOTIFY_DOC_OBSERVED_GENERAL, documentDescription,
							request.getNameFile());
					listRoles.add(Constants.ROLE_ESP_ADQ);

				} else {

					if (request.getType().compareTo(FlowConstants.DOC_EVALUATION_REPORT_ACCREDIT) == 0) {

						log.info("INFORME DE EVALUACION - LUEGO DE A ACREDITAR");

						observation = String.format(Constant.NOTIFY_DOC_EVALUATION_REPORT_FINALLY, documentDescription,
								request.getNameFile());
						listRoles.add(Constants.ROLE_ESP_ADQ);

					} else {

						if (request.getNroApproved()
								.compareTo(FlowConstants.DOC_EVALUATION_REPORT_NUMBER_APPROVED) == 0) {
							log.info("INFORME DE EVALUACION - LUEGO DE TODAS LAS APROBACIONES");

							observation = String.format(Constant.NOTIFY_DOC_EVALUATION_REPORT_ACREDITED,
									documentDescription, request.getNameFile());
							listRoles.add(Constants.ROLE_DIR_EJE);
						} else {

							log.info("INFORME DE EVALUACION - LUEGO DE ADJUNTAR O APROBAR");

							listRoles.add(Constants.ROLE_DIR_ADQ);
							listRoles.add(Constants.ROLE_DIR_ADJ);
						}

					}

					/*****************************************/

				}

				break;
			}

			break;

		case FlowConstants.STEP_SIGNED_CONTRACT:
			url = String.format(Constant.URL_PROCESS_STEP_7, process.getIdProject());

			/* Agregar FLujo de Aprobacion de Contrato Firmado */

			if (FlowConstants.DOC_SIGNED_CONTRACT.equals(request.getDocumentType())) {

				if (request.getType().compareTo(Constant.TYPE_REVIEW_OBSERVED) == 0) {

					log.info("Contrato Firmado - LUEGO DE QUE SE OBSERVO");

					observation = String.format(Constant.NOTIFY_DOC_OBSERVED_GENERAL, documentDescription,
							request.getNameFile());
					listRoles.add(Constants.ROLE_ESP_ADQ);

				} else {

					if (request.getType().compareTo(FlowConstants.DOC_SIGNED_CONTRACT_ACCREDIT) == 0) {

						log.info("CONTRATO FIRMADO - LUEGO DE A ACREDITAR");

						observation = String.format(Constant.NOTIFY_DOC_SIGNED_CONTRACT_FINALLY, documentDescription,
								request.getNameFile());
						listRoles.add(Constants.ROLE_ESP_ADQ);

					} else {

						if (request.getNroApproved()
								.compareTo(FlowConstants.DOC_SIGNED_CONTRACT_NUMBER_APPROVED) == 0) {
							log.info("CONTRATO FIRMADO - LUEGO DE TODAS LAS APROBACIONES");

							observation = String.format(Constant.NOTIFY_DOC_SIGNED_CONTRACT_ACREDITED,
									documentDescription, request.getNameFile());
							listRoles.add(Constants.ROLE_DIR_EJE);
						} else {

							log.info("CONTRATO FIRMADO - LUEGO DE ADJUNTAR O APROBAR");
							listRoles.add(Constants.ROLE_DIR_ADQ);
							listRoles.add(Constants.ROLE_DIR_ADJ);
						}

					}

					/*****************************************/

				}

				break;
			}

			break;

		}

		log.info("observation:" + observation);

		if (!StringUtils.isEmpty(observation))
			sendEmail(listRoles, observation, url);
	}

//	@Override
//	public Mono<ProcessItemResponse> acreditedDocument(String idProcess, ApproveRequest request,
//			UserSession userSession) {
//		return processDao.updateProcessAccredited(idProcess, request, userSession).flatMap(item -> {
//
//			ProcessItemResponse destiny = new ProcessItemResponse();
//			new ProcessMap().mapping(item, destiny);
//
//			updateProject(item, userSession);
//
//			sendNotifyEmail(item);
//
//			return Mono.just(destiny);
//		});
//	}

	private void updateProject(Process process, UserSession userSession) {
		Project project = new Project();
		project.setProcess(process);

		projectDao.update(process.getIdProject(), project, userSession).subscribe();
	}

}
