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
import com.invest.honduras.dao.ExecuteDao;
import com.invest.honduras.dao.ProjectDao;
import com.invest.honduras.dao.StorageDao;
import com.invest.honduras.dao.UserDao;
import com.invest.honduras.domain.map.ExecuteMap;
import com.invest.honduras.domain.map.FileMap;
import com.invest.honduras.domain.model.Project;
import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.execute.Execute;
import com.invest.honduras.domain.model.process.FlowFile;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.http.response.execution.ExecuteItemResponse;
import com.invest.honduras.notify.NotifyClient;
import com.invest.honduras.notify.request.ItemUserNotifyRequest;
import com.invest.honduras.service.ExecuteStorageService;
import com.invest.honduras.util.Constant;
import com.invest.honduras.util.Constants;
import com.invest.honduras.util.FlowConstants;
import com.invest.honduras.util.FlowUtil;
import com.invest.honduras.util.UtilDocumentFilePart;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ExecuteStorageServiceImpl implements ExecuteStorageService {

	@Autowired
	StorageDao storageDao;

	@Autowired
	ExecuteDao executeDao;

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
	public Mono<ExecuteItemResponse> create(String idExecute, List<Part> part, UserSession userSession) {

		FlowFile flowFile = UtilDocumentFilePart.getParaneter(part);

		//return FilePartUtils.getByteArray(flowFile.getFilePart()).flatMap(bytes -> {

			Storage storage = new Storage();

			new FileMap().mapping( flowFile, storage);

			return blockchain.addDocumentBlockchain(flowFile, storage, userSession).flatMap(response -> {

				if (HttpStatus.OK.value() == response.getStatus()) {

					return saveFile(idExecute, storage, flowFile, userSession);

				} else {
					
							throw new GlobalException(HttpStatus.BAD_REQUEST,response.getError());
				}
			});
		//});
	}

	private Mono<ExecuteItemResponse> saveFile(String idExecute, Storage storage, FlowFile flowFile,
			UserSession userSession) {
		return userDao.findByEmail(userSession.getEmail()).flatMap(user -> {
			return storageDao.saveStorage(storage,  flowFile.getFilePart()).flatMap(findStorage -> {
				return projectDao.findById(flowFile.getIdProject()).flatMap(project -> {

					return executeDao
							.updateExecuteStorage(flowFile, idExecute, findStorage, user)
							.flatMap(item -> {
								updateProject(item, userSession);
								ExecuteItemResponse destiny = new ExecuteItemResponse();
								sendEmailAttachment(project, storage, flowFile);
								new ExecuteMap().mapping(item, destiny);

								return Mono.just(destiny);
							});
				});
			});
		});
	}

	private void updateProject(Execute execute, UserSession userSession) {
		Project project = new Project();
		project.setExecute(execute);

		projectDao.update(execute.getIdProject(), project, userSession).subscribe();
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
		
		switch (codeStep) {

		case FlowConstants.STEP_PAYMENT:

			url = String.format(Constant.URL_EXECUTE_STEP_3, project.getId());
			if (FlowConstants.DOC_ADVANCE_REQUEST.equals(documentType)) {
				observation = isAccredited
						? String.format(Constant.NOTIFY_DOC_ADVANCE_REQUEST_F01_CDP, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName())
						: String.format(Constant.NOTIFY_DOC_ADVANCE_REQUEST, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName());

				if(isAccredited)
					listRoles.add(Constants.ROLE_DIR_ADM_FIN);
				else {
					listRoles.add(Constants.ROLE_COO_TEC);
					listRoles.add(Constants.ROLE_DIR_LEG);
				}
				
				
				break;
			}
			
			/* ADD RDELAROSA 09/06/2020 */
			if (FlowConstants.DOC_ADVANCE_PROOF_PAYMENT.equals(documentType)) {
				observation = String.format(Constant.NOTIFY_DOC_ADVANCE_REQUEST_PROOF, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName());

				listRoles.add(Constants.ROLE_COO_TEC);
				
				break;
			}
			
			/* ..................................................................*/
			
			if (FlowConstants.DOC_ESTIMATE_REQUEST.equals(documentType)) {
			
				observation = isAccredited
						? String.format(Constant.NOTIFY_DOC_ESTIMATE_REQUEST_F01_CDP, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName())
						: String.format(Constant.NOTIFY_DOC_ESTIMATE_REQUEST, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName());

				if(isAccredited)
					listRoles.add(Constants.ROLE_DIR_ADM_FIN);
				else {
					listRoles.add(Constants.ROLE_COO_TEC);
				}
				
				
				break;
			}

			
			/* ADD RDELAROSA 09/06/2020  */
			if (FlowConstants.DOC_ESTIMATE_PROOF_PAYMENT.equals(documentType)) {
				observation = String.format(Constant.NOTIFY_DOC_ESTIMATE_REQUEST_PROOF, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName());

				listRoles.add(Constants.ROLE_COO_TEC);
				break;
			}
			
			/*..................................................................................................*/
			
			
			if (FlowConstants.DOC_FINAL_PAYMENT_REQUEST.equals(documentType)) {
				
				observation = isAccredited
						? String.format(Constant.NOTIFY_DOC_FINAL_PAYMENT_F01_CDP, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName())
						: String.format(Constant.NOTIFY_DOC_FINAL_PAYMENT, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName());

				if(isAccredited)
					listRoles.add(Constants.ROLE_DIR_ADM_FIN);
				else {
					listRoles.add(Constants.ROLE_COO_TEC);
					listRoles.add(Constants.ROLE_DIR_LEG);
					
				}
				
				break;
			}

			
			/* ADD RDELAROSA 09/06/2020 */
			if (FlowConstants.DOC_FINAL_PAYMENT_PROOF_PAYMENT.equals(documentType)) {
				observation = String.format(Constant.NOTIFY_DOC_FINAL_PAYMENT_PROOF, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName());

				listRoles.add(Constants.ROLE_COO_TEC);
				break;
			}
			
			/*....................................................................................................*/
			
			
			if (FlowConstants.DOC_CONTRACT_MODIFICATE_INIT.equals(documentType)) {
				observation = String.format(Constant.NOTIFY_DOC_CONTRACT_MODIFICATE, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName());

				
				if(isAccredited) {
					/*Nothing)*/
				} else {
					listRoles.add(Constants.ROLE_COO_TEC);
					listRoles.add(Constants.ROLE_DIR_LEG);
					listRoles.add(Constants.ROLE_DIR_TRA);
				}
				
				break;
			}
			
			
			
			break;

			
			
			
		case FlowConstants.STEP_CONTRACT_MODIFICATE:
			url = String.format(Constant.URL_EXECUTE_STEP_3, project.getId());

			if (FlowConstants.DOC_CONTRACT_MODIFICATE.equals(documentType)) {
				observation = String.format(Constant.NOTIFY_DOC_CONTRACT_MODIFICATE, documentDescription, fileName,project.getSolicitude().getDataProcess().getProcessName());

				
				if(isAccredited) {
					/*Nothing)*/
				} else {
					listRoles.add(Constants.ROLE_COO_TEC);
					listRoles.add(Constants.ROLE_DIR_LEG);
					listRoles.add(Constants.ROLE_DIR_TRA);
				}
				
				break;
			}
			break;
		default:
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
					if(user.getCodeStatus().equals(Constant.USER_HABILITADO)) {

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
