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
import com.invest.honduras.config.ApplicationBlockChain;
import com.invest.honduras.config.ConfigurationProject;
import com.invest.honduras.dao.ExecuteDao;
import com.invest.honduras.dao.ProjectDao;
import com.invest.honduras.dao.UserDao;
import com.invest.honduras.domain.map.ExecuteMap;
import com.invest.honduras.domain.model.Project;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.execute.Execute;
import com.invest.honduras.enums.TypeStatusCode;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.http.client.ExecuteCreateRequest;
import com.invest.honduras.http.client.execute.ExecuteRequest;
import com.invest.honduras.http.client.process.ApproveRequest;
import com.invest.honduras.http.response.UserItemResponse;
import com.invest.honduras.http.response.execution.ExecuteItemResponse;
import com.invest.honduras.notify.NotifyClient;
import com.invest.honduras.notify.request.ItemUserNotifyRequest;
import com.invest.honduras.service.ExecuteService;
import com.invest.honduras.util.Constant;
import com.invest.honduras.util.Constants;
import com.invest.honduras.util.FlowConstants;
import com.invest.honduras.util.FlowUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ExecuteServiceImpl implements ExecuteService {

	@Autowired
	ExecuteDao executeDao;

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

	@Autowired
	ApplicationBlockChain app;

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
	public Mono<ExecuteItemResponse> findByIdProject(String idProject) {
		return executeDao.findByIdProject(idProject).map(item -> {
			ExecuteItemResponse destiny = new ExecuteItemResponse();
			new ExecuteMap().mapping(item, destiny);
			return destiny;
		});
	}

	@Override
	public Mono<ExecuteItemResponse> findById(String id) {
		return executeDao.findById(id).map(item -> {
			ExecuteItemResponse destiny = new ExecuteItemResponse();
			new ExecuteMap().mapping(item, destiny);
			return destiny;
		});
	}
	
	private void validateRoleProject(String role, String proxy) {
		
		try {
			if(Boolean.FALSE.equals(app.getIRoles().hasRoleUser(role, proxy))) {
				throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.MESSAGE_USER_NOT_ROLE.name()+":"+role);
			}
			
			if(Boolean.FALSE.equals(app.getIRoles().hasRoleUser(Constant.ROLE_COO_TEC, proxy))) {
				throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.MESSAGE_USER_NOT_ROLE.name()+":"+Constant.ROLE_COO_TEC);
			}
		} catch (Exception e) {
			throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

		}
		
	}
	

	@Override
	public Mono<ExecuteItemResponse> updateExecute(String id, ExecuteRequest request, UserSession userSession) {

		return userDao.findByEmail(userSession.getEmail()).flatMap(user -> {

			return projectDao.findById(request.getIdProject()).flatMap(project -> {

				validateRoleProject(userSession.getRole(), user.getProxyAddress()) ;
				

						return blockChain.setChangeStepBlockchain(project.getIdAccredited(), user.getProxyAddress(),
								userSession.getRole(), request.getCodeStep()).flatMap(response -> {

									addUserRoleProject(project, request, user.getProxyAddress());

									if (HttpStatus.OK.value() == response.getStatus()) {

										return saveUpdateExecute(project, id, request, userSession);
									} else {
										throw new GlobalException(HttpStatus.BAD_REQUEST, response.getError());
									}

								});
			});

		});
	}

	private void addUserRoleProject(Project project, ExecuteRequest request, String proxyUserSession) {
		if (request.getCodeStep().equals(FlowConstants.STEP_CONTRACT_SUPERVISOR)) {

			if (request.getAssignContractor().getContractor() != null
					&& !request.getAssignContractor().getContractor().isEmpty()) {

				userDao.findUserByIds(request.getAssignContractor().getContractor()).subscribe(user -> {

					blockChain.addUserRoleProject(project.getIdAccredited(), user.getProxyAddress(),
							Constants.ROLE_CONT, proxyUserSession);

				});
			}

			if (request.getAssignContractor().getSupervising() != null
					&& !request.getAssignContractor().getSupervising().isEmpty()) {

				userDao.findUserByIds(request.getAssignContractor().getSupervising()).subscribe(user -> {
					blockChain.addUserRoleProject(project.getIdAccredited(), user.getProxyAddress(), Constants.ROLE_SUP,
							proxyUserSession);
				});
			}
		}
	}

	public Mono<ExecuteItemResponse> saveUpdateExecute(Project project, String id, ExecuteRequest request,
			UserSession userSession) {

		return executeDao.updateExecute(id, request, userSession).flatMap(item -> {

			updateProject(item, userSession);
			ExecuteItemResponse destiny = new ExecuteItemResponse();
			new ExecuteMap().mapping(item, destiny);

			sendNotifyEmailUpdate(project, request.getCodeStep());

			return Mono.just(destiny);
		});
	}

	@Override
	public Mono<ExecuteItemResponse> approveDocument(String idExecute, ApproveRequest request, UserSession userSession,
			boolean flag) {

		String hashComment = HashUtils.convetStringToHashKeccakHex(request.getObservation().getBytes());

		return blockChain.approveDocumentBlockchain(request, idExecute, userSession, hashComment).flatMap(response -> {

			if (HttpStatus.OK.value() == response.getStatus()) {

				return saveApproveDocument(idExecute, request, userSession, hashComment, flag);

			} else {
				throw new GlobalException(HttpStatus.BAD_REQUEST, response.getError());
			}
		});
	}

	private Mono<ExecuteItemResponse> saveApproveDocument(String idExecute, ApproveRequest request,
			UserSession userSession, String hash, boolean flag) {
		return userDao.findByEmail(userSession.getEmail()).flatMap(user -> {
			return executeDao.updateExecuteApproved(idExecute, hash, request, user, flag).flatMap(item -> {

				ExecuteItemResponse destiny = new ExecuteItemResponse();
				new ExecuteMap().mapping(item, destiny);

				updateProject(item, userSession);

				sendEmailApprove(request);

				return Mono.just(destiny);
			});
		});
	}

	private void updateProject(Execute execute, UserSession userSession) {
		Project project = new Project();
		project.setExecute(execute);

		projectDao.update(execute.getIdProject(), project, userSession).subscribe();
	}

	private void updateProjectCreate(Execute execute, UserSession userSession) {
		Project project = new Project();
		project.setExecute(execute);
		project.setCurrentStatus(Constant.TypeStatus.EJECUCION.getType());

		projectDao.update(execute.getIdProject(), project, userSession).subscribe();
	}

	@Override
	public Mono<List<UserItemResponse>> getUsersByRole(String role) {

		return userDao.findUserByRol(role).collectList().flatMap(list -> {

			List<UserItemResponse> listUser = new ArrayList<>();

			list.forEach(i -> {

				UserItemResponse response = new UserItemResponse();
				response.setId(i.getId());
				response.setFullname(i.getFullname());
				response.setSurnames(i.getSurnames());
				listUser.add(response);
			});

			return Mono.just(listUser);
		});
	}

	@Override
	public Mono<ExecuteItemResponse> createExecute(String idProject, ExecuteCreateRequest request,
			UserSession userSession) {
		return userDao.findByEmail(userSession.getEmail()).flatMap(user -> {

			return projectDao.findById(idProject).flatMap(project -> {
				return blockChain.setChangeStepBlockchain(project.getIdAccredited(), user.getProxyAddress(),
						userSession.getRole(), request.getCodeStep()).flatMap(response -> {
							if (HttpStatus.OK.value() == response.getStatus()) {
								return saveCreateExecute(project, request, userSession);
							} else {
								throw new GlobalException(HttpStatus.BAD_REQUEST, response.getError());
							}

						});
			});

		});
	}

	private Mono<ExecuteItemResponse> saveCreateExecute(Project project, ExecuteCreateRequest request,
			UserSession userSession) {
		Execute execute = new Execute();

		new ExecuteMap().mappingCreate(project.getId(), request, execute, userSession);

		return executeDao.createExecute(execute).flatMap(item -> {

			updateProjectCreate(item, userSession);
			
			ExecuteItemResponse destiny = new ExecuteItemResponse();
			new ExecuteMap().mapping(item, destiny);

			sendNotifyEmailCreate(project);

			return Mono.just(destiny);
		});
	}

	private void sendNotifyEmailCreate(Project project) {
		String nameProcess = project.getSolicitude().getDataProcess().getProcessName();

		String observation = String.format(Constant.NOTIFY_EXECUTE_INIT, nameProcess);

		List<String> listRoles = new ArrayList<>();

		listRoles.add(Constants.ROLE_COO_TEC);
		sendEmail(listRoles, observation, Constant.URL_HOME_PROCESS);
	}

	private void sendNotifyEmailUpdate(Project project, String codeStep) {
		String observation = "";
		String nameProcess = project.getSolicitude().getDataProcess().getProcessName();

		List<String> listRoles = new ArrayList<>();
		String url= "";
		switch (codeStep) {

		case FlowConstants.STEP_CONTRACT_MODIFICATE:
			url = String.format(Constant.URL_EXECUTE_STEP_5, project.getId());
			observation = String.format(Constant.NOTIFY_STEP_MODIFY_CONTRACT, nameProcess);

			listRoles.add(Constants.ROLE_COO_TEC);

			break;

		case FlowConstants.STEP_QUALITY_GUARANTEE:
			url = Constant.URL_HOME_PROCESS;

			observation = String.format(Constant.NOTIFY_STEP_STEP_QUALITY_GUARANTEE, nameProcess);

			listRoles.add(Constants.ROLE_COO_TEC);

			break;
		default:
			break;
		}

		if (!StringUtils.isEmpty(observation))
			sendEmail(listRoles, observation, url);
	}

	private void sendEmailApprove(ApproveRequest request) {

		String documentDescription = FlowUtil.getDescriptionByCodeDocument(config.getRule(), request.getDocumentType());

		String observation = String.format(Constant.NOTIFY_DOC_APPROVE_GENERAL, documentDescription,
				request.getNameFile());

		log.info("NRO APRROVED:" + request.getNroApproved());

		log.info("TIPO DE APROBACION:" + request.getType());

		List<String> listRoles = new ArrayList<>();

		String url = "";
		switch (request.getCodeStep()) {

		case FlowConstants.STEP_PAYMENT:
			url = String.format(Constant.URL_EXECUTE_STEP_3, request.getIdProject());
			if (FlowConstants.DOC_ADVANCE_REQUEST.equals(request.getDocumentType())) {
				
				if (request.getType().compareTo(Constant.TYPE_REVIEW_OBSERVED) == 0) {

					log.info("Solicitud de Anticipo - LUEGO DE QUE SE OBSERVO");

					observation = String.format(Constant.NOTIFY_DOC_OBSERVED_GENERAL, documentDescription,
							request.getNameFile());
					listRoles.add(Constants.ROLE_COO_TEC);

				} else {

					/*********** ADD RDELAROSA 23052020 **********/
					if (request.getType().compareTo(FlowConstants.DOC_ADVANCE_REQUEST_ACCREDIT) == 0) {

						log.info("DOCUMENTO SOLICITUD DE ANTICIPO - LUEGO DE A ACREDITAR");

						observation = String.format(Constant.NOTIFY_DOC_ADVANCE_REQUEST_FINALLY, documentDescription,
								request.getNameFile());
						listRoles.add(Constants.ROLE_COO_TEC);

					} else {

						if (request.getNroApproved().compareTo(FlowConstants.DOC_ADVANCE_REQUEST_NUMBER_APPROVED) == 0)

						{
							log.info("DOCUMENTO SOLICITUD DE ANTICIPO - LUEGO DE TODAS LAS APROBACIONES");

							observation = String.format(Constant.NOTIFY_DOC_ADVANCE_REQUEST_ACREDITED,
									documentDescription, request.getNameFile());
							listRoles.add(Constants.ROLE_DIR_TRA);
						} else {

							log.info("DOCUMENTO SOLICITUD DE ANTICIPO - LUEGO DE ADJUNTAR O APROBAR");

							listRoles.add(Constants.ROLE_DIR_LEG);
							listRoles.add(Constants.ROLE_COO_TEC);
						}

					}

					/*****************************************/

				}

				break;
			}

			if (FlowConstants.DOC_ESTIMATE_REQUEST.equals(request.getDocumentType())) {

				if (request.getType().compareTo(Constant.TYPE_REVIEW_OBSERVED) == 0) {

					log.info("Estimacion de Pago - LUEGO DE QUE SE OBSERVO");

					observation = String.format(Constant.NOTIFY_DOC_OBSERVED_GENERAL, documentDescription,
							request.getNameFile());
					listRoles.add(Constants.ROLE_COO_TEC);

				} else {

					/*********** ADD RDELAROSA 23052020 **********/
					if (request.getType().compareTo(FlowConstants.DOC_ESTIMATE_REQUEST_ACCREDIT) == 0) {

						log.info("DOCUMENTO ESTIMACION DE PAGO - LUEGO DE A ACREDITAR");

						observation = String.format(Constant.NOTIFY_DOC_ESTIMATE_REQUEST_FINALLY, documentDescription,
								request.getNameFile());
						listRoles.add(Constants.ROLE_COO_TEC);

					} else {

						if (request.getNroApproved().compareTo(FlowConstants.DOC_ESTIMATE_REQUEST_NUMBER_APPROVED) == 0)

						{
							log.info("DOCUMENTO ESTIMACION DE PAGO - LUEGO DE TODAS LAS APROBACIONES");

							observation = String.format(Constant.NOTIFY_DOC_ESTIMATE_REQUEST_ACREDITED,
									documentDescription, request.getNameFile());
							listRoles.add(Constants.ROLE_DIR_TRA);
						} else {

							log.info("DOCUMENTO ESTIMACION DE PAGO - LUEGO DE ADJUNTAR O APROBAR");

							listRoles.add(Constants.ROLE_COO_TEC);
						}

					}

					/*****************************************/

				}

				break;
			}

			if (FlowConstants.DOC_FINAL_PAYMENT_REQUEST.equals(request.getDocumentType())) {

				if (request.getType().compareTo(Constant.TYPE_REVIEW_OBSERVED) == 0) {

					log.info("Pago Final - LUEGO DE QUE SE OBSERVO");

					observation = String.format(Constant.NOTIFY_DOC_OBSERVED_GENERAL, documentDescription,
							request.getNameFile());
					listRoles.add(Constants.ROLE_COO_TEC);

				} else {

					/*********** ADD RDELAROSA 23052020 **********/
					if (request.getType().compareTo(FlowConstants.DOC_FINAL_PAYMENT_REQUEST_ACCREDIT) == 0) {

						log.info("DOCUMENTO PAGO FINAL - LUEGO DE A ACREDITAR");

						observation = String.format(Constant.NOTIFY_DOC_FINAL_PAYMENT_FINALLY, documentDescription,
								request.getNameFile());
						listRoles.add(Constants.ROLE_COO_TEC);

					} else {

						if (request.getNroApproved()
								.compareTo(FlowConstants.DOC_FINAL_PAYMENT_REQUEST_NUMBER_APPROVED) == 0)

						{
							log.info("DOCUMENTO PAGO FINAL - LUEGO DE TODAS LAS APROBACIONES");

							observation = String.format(Constant.NOTIFY_DOC_FINAL_PAYMENT_ACREDITED,
									documentDescription, request.getNameFile());
							listRoles.add(Constants.ROLE_DIR_TRA);
						} else {

							log.info("DOCUMENTO PAGO FINAL - LUEGO DE ADJUNTAR O APROBAR");

							listRoles.add(Constants.ROLE_COO_TEC);
							listRoles.add(Constants.ROLE_DIR_LEG);
						}

					}

					/*****************************************/

				}

				break;
			}

			if (FlowConstants.DOC_CONTRACT_MODIFICATE_INIT.equals(request.getDocumentType())) {

				if (request.getType().compareTo(Constant.TYPE_REVIEW_OBSERVED) == 0) {

					log.info("Modificacion de Contrato1 - LUEGO DE QUE SE OBSERVO");

					observation = String.format(Constant.NOTIFY_DOC_OBSERVED_GENERAL, documentDescription,
							request.getNameFile());
					listRoles.add(Constants.ROLE_COO_TEC);

				} else {

					/*********** ADD RDELAROSA 25052020 **********/
					if (request.getType().compareTo(FlowConstants.DOC_CONTRACT_MODIFICATE_ACCREDIT) == 0) {

						log.info("DOCUMENTO MODIFICACION DE CONTRATO - LUEGO DE A ACREDITAR");

						observation = String.format(Constant.NOTIFY_DOC_CONTRACT_MODIFICATE_FINALLY,
								documentDescription, request.getNameFile());
						listRoles.add(Constants.ROLE_COO_TEC);

					} else {

						if (request.getNroApproved()
								.compareTo(FlowConstants.DOC_CONTRACT_MODIFICATE_NUMBER_APPROVED) == 0)

						{
							log.info("DOCUMENTO MODIFICACION DE CONTRATO - LUEGO DE TODAS LAS APROBACIONES");

							observation = String.format(Constant.NOTIFY_DOC_CONTRACT_MODIFICATE_ACREDITED,
									documentDescription, request.getNameFile());
							listRoles.add(Constants.ROLE_DIR_EJE);
						} else {

							log.info("DOCUMENTO MODIFICACION DE CONTRATO - LUEGO DE ADJUNTAR O APROBAR");

							listRoles.add(Constants.ROLE_COO_TEC);
							listRoles.add(Constants.ROLE_DIR_LEG);
							listRoles.add(Constants.ROLE_DIR_TRA);
						}

					}

					/*****************************************/

				}

				break;

			}

			break;

		case FlowConstants.STEP_CONTRACT_MODIFICATE:
			
			url = String.format(Constant.URL_EXECUTE_STEP_3, request.getIdProject());

			if (FlowConstants.DOC_CONTRACT_MODIFICATE.equals(request.getDocumentType())) {

				if (request.getType().compareTo(Constant.TYPE_REVIEW_OBSERVED) == 0) {

					log.info("Modificacion de Contrato2 - LUEGO DE QUE SE OBSERVO");

					observation = String.format(Constant.NOTIFY_DOC_OBSERVED_GENERAL, documentDescription,
							request.getNameFile());
					listRoles.add(Constants.ROLE_COO_TEC);

				} else {

					/*********** ADD RDELAROSA 25052020 **********/
					if (request.getType().compareTo(FlowConstants.DOC_CONTRACT_MODIFICATE_ACCREDIT) == 0) {

						log.info("PASO 4 - DOCUMENTO MODIFICACION DE CONTRATO - LUEGO DE A ACREDITAR");

						observation = String.format(Constant.NOTIFY_DOC_CONTRACT_MODIFICATE_FINALLY,
								documentDescription, request.getNameFile());
						listRoles.add(Constants.ROLE_COO_TEC);

					} else {

						if (request.getNroApproved()
								.compareTo(FlowConstants.DOC_CONTRACT_MODIFICATE_NUMBER_APPROVED) == 0)

						{
							log.info("PASO 4 - DOCUMENTO MODIFICACION DE CONTRATO - LUEGO DE TODAS LAS APROBACIONES");

							observation = String.format(Constant.NOTIFY_DOC_CONTRACT_MODIFICATE_ACREDITED,
									documentDescription, request.getNameFile());
							listRoles.add(Constants.ROLE_DIR_EJE);
						} else {

							log.info("PASO 4 - DOCUMENTO MODIFICACION DE CONTRATO - LUEGO DE ADJUNTAR O APROBAR");

							listRoles.add(Constants.ROLE_COO_TEC);
							listRoles.add(Constants.ROLE_DIR_LEG);
							listRoles.add(Constants.ROLE_DIR_TRA);
						}

					}

					/*****************************************/

				}

				break;

			}
			break;

		}

		if (!StringUtils.isEmpty(observation))
			sendEmail(listRoles, observation, url);
	}

	private void sendEmail(List<String> listRoles, String mesage, String urlPath) {
		userDao.findUserByRoles(listRoles).collectList().subscribe(list -> {

			list.forEach(user -> {
				if (!StringUtils.isEmpty(user.getEmail())) {

					log.info("codeStatus ..." + user.getCodeStatus());
					if (user.getCodeStatus().equals(Constant.USER_HABILITADO)) {
						log.info("sendEmailAccredited ..." + user.getEmail());

						ItemUserNotifyRequest request = new ItemUserNotifyRequest();
						request.setEmail(user.getEmail());
						request.setUserId(user.getId());
						request.setName(user.getFullname());

						request.setObs(mesage);
						request.setTemplate(Constant.TYPE_TEMPLATE);
						request.setSubject(Constant.NOTIFY_SUBJET);
						request.setDetailsURL(config.getConfiguration().getUrlLogin()+urlPath);

						notifyClient.sendUpdateNotify(request);

					}
				}
			});

		});
	}

}
