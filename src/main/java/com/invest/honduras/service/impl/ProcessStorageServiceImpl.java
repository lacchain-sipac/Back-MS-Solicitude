package com.invest.honduras.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.invest.honduras.blockchain.StepManagerBlockChain;
import com.invest.honduras.config.ConfigurationProject;
import com.invest.honduras.dao.ProcessDao;
import com.invest.honduras.dao.ProjectDao;
import com.invest.honduras.dao.StorageDao;
import com.invest.honduras.dao.UserDao;
import com.invest.honduras.domain.map.FileMap;
import com.invest.honduras.domain.map.ProcessMap;
import com.invest.honduras.domain.model.Project;
import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.process.FlowFile;
import com.invest.honduras.domain.model.process.Process;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.http.response.process.ProcessItemResponse;
import com.invest.honduras.notify.NotifyClient;
import com.invest.honduras.notify.request.ItemUserNotifyRequest;
import com.invest.honduras.service.ProcessStorageService;
import com.invest.honduras.util.Constant;
import com.invest.honduras.util.Constants;
import com.invest.honduras.util.FlowConstants;
import com.invest.honduras.util.FlowUtil;
import com.invest.honduras.util.UtilDocumentFilePart;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProcessStorageServiceImpl implements ProcessStorageService {

	@Autowired
	StorageDao storageDao;

	@Autowired
	ProcessDao processDao;

	@Autowired
	ProjectDao projectDao;

	@Autowired
	UserDao userDao;

	@Autowired
	StepManagerBlockChain blockchain;

	@Autowired
	NotifyClient notifyClient;

	@Autowired
	ConfigurationProject config;

	@Override
	public Mono<Storage> findById(String id) {
		return storageDao.findById(id);
	}

	@Override
	public Mono<ProcessItemResponse> create(String idProcess, List<Part> part, UserSession userSession) {

		FlowFile flowFile = UtilDocumentFilePart.getParaneter(part);

		//return FilePartUtils.getByteArray(flowFile.getFilePart()).flatMap(bytes -> {
			Storage storage = new Storage();

			new FileMap().mapping( flowFile, storage);

			return blockchain.addDocumentBlockchain(flowFile, storage, userSession).flatMap(response -> {

				if (HttpStatus.OK.value() == response.getStatus()) {

					return saveFile(idProcess, storage, flowFile, userSession);

				} else {
					throw new GlobalException(HttpStatus.BAD_REQUEST,
							response.getError());
				}
			});

		//});
	}

	private Mono<ProcessItemResponse> saveFile(String idProcess, Storage storage, FlowFile flowFile,
			UserSession userSession) {
		return userDao.findByEmail(userSession.getEmail()).flatMap(user -> {
			return storageDao.saveStorage(storage, flowFile.getFilePart()).flatMap(findStorage -> {
				return projectDao.findById(flowFile.getIdProject()).flatMap(project -> {
					return processDao.updateProcessStorage(idProcess, flowFile, findStorage, user).flatMap(item -> {
						updateProject(item, userSession);

						ProcessItemResponse destiny = new ProcessItemResponse();
						sendEmailAttachment(project, storage, flowFile);
						new ProcessMap().mapping(item, destiny);

						return Mono.just(destiny);
					});
				});
			});
		});
	}

	private void updateProject(Process process, UserSession userSession) {
		Project project = new Project();
		project.setProcess(process);

		projectDao.update(process.getIdProject(), project, userSession).subscribe();
	}

	private void sendEmailAttachment(Project project, Storage storage, FlowFile flowFile) {
		String codeStep = flowFile.getCodeStep();
		String documentDescription = FlowUtil.getDescriptionByCodeDocument(config.getRule(),
				flowFile.getDocumentType());
		boolean isAccredited = flowFile.isAccredited();
		List<String> listRoles = new ArrayList<>();
		String fileName = storage.getFileName();
		String observation = "";
		String documentType = flowFile.getDocumentType();
		
		String url = "";

		log.info("ProccesStorageIpmpl - sendmailAttachment - Nombre Proceso :"+project.getSolicitude().getDataProcess().getProcessName()); 
		
		switch (codeStep) {

		case FlowConstants.STEP_START_PROCESS:

			url = String.format(Constant.URL_PROCESS_STEP_1, project.getId());

			if (FlowConstants.DOC_BIDDING_ATTACH.equals(documentType)) {
				
				observation = isAccredited
						? String.format(Constant.NOTIFY_DOC_BIDDING_NO_OBJECTION, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName())
						: String.format(Constant.NOTIFY_DOC_ATTACH, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName());
					
				/*ADD RDELAROSA 06052020 */
				if(isAccredited)
					listRoles.add(Constants.ROLE_ASI_DIR_EJE);
				else {
					listRoles.add(Constants.ROLE_DIR_ADQ);
					listRoles.add(Constants.ROLE_DIR_ADJ);
				}
				/******************************************/
				
				break;
				
			}

			
			/* ADD RDELAROSA 06/06/2020 */
			if (FlowConstants.DOC_BIDDING_RESPONSE_NO_OBJECTION.equals(documentType)) {
				
				observation = String.format(Constant.NOTIFY_DOC_BIDDING_RESPONSE_NO_OBJECTION, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName());

				listRoles.add(Constants.ROLE_ESP_ADQ);
				break;
			}
			/****************************************************************/
			

			if (FlowConstants.DOC_CLARIFICATION_REQUEST_.equals(documentType)) {
				observation = String.format(Constant.NOTIFY_DOC_CLARIFICATION_REQUEST, documentDescription, fileName);

				listRoles.add(Constants.ROLE_ESP_ADQ);
				break;
			}
			if (FlowConstants.DOC_CLARIFICATION_RESPONSE.equals(documentType)) {
				
				observation = String.format(Constant.NOTIFY_CLARIFICATION_RESPONSE, documentDescription, fileName);
				
				/*ADD RDELAROSA 06052020 */
				if(isAccredited) {
					/*Nothing)*/
				} else {
					listRoles.add(Constants.ROLE_COO_TEC);
					listRoles.add(Constants.ROLE_DIR_ADQ);
					listRoles.add(Constants.ROLE_DIR_ADJ);
				}
				/******************************************/
				
				break;
			}
			
			if (FlowConstants.DOC_AMENDMENT_REQUEST.equals(documentType)) {
				observation = String.format(Constant.NOTIFY_AMENDMENT_REQUEST, documentDescription, fileName);

				listRoles.add(Constants.ROLE_ESP_ADQ);
				break;
			}

			if (FlowConstants.DOC_AMENDMENT_RESPONSE.equals(documentType)) {
				observation = isAccredited
						? String.format(Constant.NOTIFY_AMENDMENT_RESPONSE_APPROVE, documentDescription, fileName)
						: String.format(Constant.NOTIFY_AMENDMENT_RESPONSE, documentDescription, fileName);

				/*ADD RDELAROSA 06052020 */
				if(isAccredited) {
					listRoles.add(Constants.ROLE_ASI_DIR_EJE);
				} else {
					listRoles.add(Constants.ROLE_COO_TEC);
					listRoles.add(Constants.ROLE_DIR_ADQ);
					listRoles.add(Constants.ROLE_DIR_ADJ);
				}
				/**************************************/
				
				
				break;
			}

			/* QUIT RDELAROSA 06/05/2020 */
			if (FlowConstants.DOC_AMENDMENT_RESPONSE_NO_OBJECTION.equals(documentType)) {
				observation = String.format(Constant.NOTIFY_AMENDMENT_RESPONSE_NO_OBJECTION, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName());

				listRoles.add(Constants.ROLE_ESP_ADQ);
				break;
			}
			/*******************************************************************/
			
			break;

		case FlowConstants.STEP_OFFER_DOCUMENTATION:
			
			/* QUIT RDELAROSA 06/05/2020
			if (FlowConstants.DOC_ACT_OPPENING.equals(documentType)) {
				
				observation = isAccredited ? Constant.NOTIFY_DOC_ACT_OPPENING_APPROVE
						: String.format(Constant.NOTIFY_DOC_ACT_OPPENING, documentDescription, fileName);

				listRoles.add(Constants.ROLE_DIR_EJE);
				break;
			}
			***************************************/
			
			break;

		case FlowConstants.STEP_EVALUATION_REPORT:
			url = String.format(Constant.URL_PROCESS_STEP_4, project.getId());

			if (FlowConstants.DOC_EVALUATION_REPORT.equals(documentType)) {
				observation = isAccredited
						? String.format(Constant.NOTIFY_DOC_EVALUATION_REPORT_NO_OBJECTION, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName())
						: String.format(Constant.NOTIFY_DOC_EVALUATION_REPORT, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName());

				
				/*ADD RDELAROSA 15052020 */
				if(isAccredited) {
					listRoles.add(Constants.ROLE_ASI_DIR_EJE);
				} else {
					listRoles.add(Constants.ROLE_DIR_ADQ);
					listRoles.add(Constants.ROLE_DIR_ADJ);
				}
				/**************************************/
				
				break;
			}
			
			
			/* QUIT RDELAROSA 15/05/2020 */
			if (FlowConstants.DOC_EVALUATION_REPORT_RESPONSE_NO_OBJECTION.equals(documentType)) {

				observation = String.format(Constant.NOTIFY_DOC_EVALUATION_REPORT_RESPONSE_NO_OBJECTION, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName());

				listRoles.add(Constants.ROLE_DIR_ADM_FIN);
				
				break;
			}
			/*..........................................................................*/

		case FlowConstants.STEP_AWARD_RESOLUTION:
			
			/* QUIT RDELAROSA 06/05/2020
			if (FlowConstants.DOC_AWARD_RESOLUTION.equals(documentType)) {
				observation = isAccredited ? Constant.NOTIFY_DOC_AWARD_RESOLUTION_APPROVE
						: String.format(Constant.NOTIFY_DOC_AWARD_RESOLUTION, documentDescription, fileName);

				listRoles.add(Constants.ROLE_ASI_DIR_EJE);
				break;
			}
			*/
			
			/* REVISAR  CONTRATO FIRMADO*/

		case FlowConstants.STEP_SIGNED_CONTRACT:
			url = String.format(Constant.URL_PROCESS_STEP_7, project.getId());

			if (FlowConstants.DOC_SIGNED_CONTRACT.equals(documentType)) {
				observation = String.format(Constant.NOTIFY_DOC_SIGNED_CONTRACT, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName());

				
				/*ADD RDELAROSA 15052020 */
				if(isAccredited) {
					/*Nothing*/
				} else {
					listRoles.add(Constants.ROLE_DIR_ADQ);
					listRoles.add(Constants.ROLE_DIR_ADJ);
				}
				/**************************************/
				break;
				
			}
			break;
			
		default:
			break;
		}
		if (!StringUtils.isEmpty(observation))
			sendEmail(listRoles, observation, url);
	}

	private void sendEmail(List<String> listRoles, String mesage,  String relativePath) {
		userDao.findUserByRoles(listRoles).collectList().subscribe(list -> {

			list.forEach(user -> {
				
				log.info("codeStatus ..." + user.getCodeStatus());
				if(user.getCodeStatus().equals(Constant.USER_HABILITADO)) {

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

}
