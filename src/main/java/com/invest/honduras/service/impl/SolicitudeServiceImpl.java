package com.invest.honduras.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.invest.honduras.blockchain.StepManagerBlockChain;
import com.invest.honduras.config.ConfigurationProject;
import com.invest.honduras.dao.ProcessDao;
import com.invest.honduras.dao.ProjectDao;
import com.invest.honduras.dao.SolicitudeDao;
import com.invest.honduras.dao.UserDao;
import com.invest.honduras.domain.map.ProcessMap;
import com.invest.honduras.domain.map.ProjectMap;
import com.invest.honduras.domain.map.SolicitudeMap;
import com.invest.honduras.domain.model.Project;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.process.Process;
import com.invest.honduras.domain.model.solicitude.ApproveSolicitudeRequest;
import com.invest.honduras.domain.model.solicitude.Solicitude;
import com.invest.honduras.enums.TypeStatusCode;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.http.client.solicitude.SolicitudeUpdateRequest;
import com.invest.honduras.http.response.solicitude.SolicitudeItemResponse;
import com.invest.honduras.notify.NotifyClient;
import com.invest.honduras.notify.request.ItemUserNotifyRequest;
import com.invest.honduras.service.SolicitudeService;
import com.invest.honduras.util.Constant;
import com.invest.honduras.util.Constants;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class SolicitudeServiceImpl implements SolicitudeService {

	@Autowired
	SolicitudeDao solicitudeDao;

	@Autowired
	ProjectDao projectDao;

	@Autowired
	ProcessDao processDao;

	@Autowired
	NotifyClient notifyClient;

	@Autowired
	UserDao userDao;

	@Autowired
	ConfigurationProject config;

	@Autowired
	StepManagerBlockChain blockChain;

	@Override
	public Mono<SolicitudeItemResponse> findById(String id) {
		return solicitudeDao.findById(id).map(solicitude -> {
			SolicitudeItemResponse destiny = new SolicitudeItemResponse();
			new SolicitudeMap().mapping(solicitude, destiny);
			return destiny;
		});
	}

	@Override
	public Mono<SolicitudeItemResponse> createSolicitude(UserSession userSession) {

		final SolicitudeMap solicitudeMap = new SolicitudeMap();
		Solicitude solicitude = new Solicitude();

		solicitudeMap.mapCreate(solicitude, userSession);

		return solicitudeDao.createSolicitude(solicitude).flatMap(item -> {

			return saveProject(item, userSession).flatMap(project -> {
				SolicitudeItemResponse destiny = new SolicitudeItemResponse();

				item.setIdProject(project.getId());
				solicitudeMap.mapping(item, destiny);

				return Mono.just(destiny);

			});

		});
	}

	@Override
	public Mono<SolicitudeItemResponse> updateSolicitude(String id, SolicitudeUpdateRequest solicitudeUpdateRequest,
			UserSession userSession) {

		return solicitudeDao.updateSolicitude(id, solicitudeUpdateRequest, userSession).flatMap(item -> {

			updateProject(item, userSession);

			SolicitudeItemResponse destiny = new SolicitudeItemResponse();
			new SolicitudeMap().mapping(item, destiny);

			sendNotifyEmail(item, userSession);

			return Mono.just(destiny);
		});
	}

	private void sendNotifyEmail(Solicitude solicitude, UserSession userSession) {

		log.info(" fhinish ==>" + solicitude.isFinishSolicitude());
		log.info("step ==>" + solicitude.getCurrentStep());
		log.info("Role ==>" + userSession.getRole());

		if (!solicitude.isFinishSolicitude() && solicitude.getCurrentStep() == Constant.STEP_FIVE
				&& Constants.ROLE_COO_TEC.equals(userSession.getRole())) {

			userDao.findUserByRol(Constants.ROLE_DIR_ADM_FIN).collectList().subscribe(list -> {

				list.forEach(user -> {

					log.info("codeStatus ..." + user.getCodeStatus());
					if (user.getCodeStatus().equals(Constant.USER_HABILITADO)) {
						if (!StringUtils.isEmpty(user.getEmail())) {

							log.info("solicitude.ROLE_DIR_ADM_FIN ..." + user.getEmail());

							ItemUserNotifyRequest request = new ItemUserNotifyRequest();
							request.setEmail(user.getEmail());
							request.setUserId(user.getId());
							request.setName(user.getFullname());
							request.setObs(String.format(Constant.NOTIFY_SOLICITUDE_STEP_4,
									solicitude.getDataProcess().getProcessName()));
							request.setTemplate(Constant.TYPE_TEMPLATE);
							request.setSubject(Constant.NOTIFY_SUBJET);

							String url = config.getConfiguration().getUrlLogin()
									+ String.format(Constant.URL_SOLICITUDE_STEP_5, solicitude.getIdProject());

							request.setDetailsURL(url);

							notifyClient.sendUpdateNotify(request);
						}
					}
				});

			});
		}

		if (solicitude.getCurrentStep() == Constant.STEP_FIVE
				&& Constants.ROLE_DIR_ADM_FIN.equals(userSession.getRole())) {

			userDao.findUserByRol(Constants.ROLE_COO_TEC).collectList().subscribe(list -> {

				list.forEach(user -> {
					log.info("codeStatus ..." + user.getCodeStatus());
					if (user.getCodeStatus().equals(Constant.USER_HABILITADO)) {

						if (!StringUtils.isEmpty(user.getEmail())) {
							log.info("solicitude.ROLE_COO_TEC ..." + user.getEmail());

							ItemUserNotifyRequest request = new ItemUserNotifyRequest();
							request.setEmail(user.getEmail());
							request.setUserId(user.getId());
							request.setName(user.getFullname());
							request.setObs(String.format(Constant.NOTIFY_SOLICITUDE_STEP_5,
									solicitude.getDataProcess().getProcessName()));
							request.setTemplate(Constant.TYPE_TEMPLATE);
							request.setSubject(Constant.NOTIFY_SUBJET);

							String url = config.getConfiguration().getUrlLogin()
									+ String.format(Constant.URL_SOLICITUDE_STEP_5, solicitude.getIdProject());

							request.setDetailsURL(url);
							// request.setDetailsURL(config.getConfiguration().getUrlLogin());

							notifyClient.sendUpdateNotify(request);
						}
					}
				});

			});
		}

		if (solicitude.isFinishSolicitude() && Constants.ROLE_COO_TEC.equals(userSession.getRole())) {

			userDao.findUserByRol(Constants.ROLE_DIR_ADJ).collectList().subscribe(list -> {

				list.forEach(user -> {
					log.info("codeStatus ..." + user.getCodeStatus());
					if (user.getCodeStatus().equals(Constant.USER_HABILITADO)) {
						if (!StringUtils.isEmpty(user.getEmail())) {

							log.info("solicitude.ROLE_DIR_ADJ ..." + user.getEmail());

							ItemUserNotifyRequest request = new ItemUserNotifyRequest();
							request.setUserId(user.getId());
							request.setName(user.getFullname());
							request.setEmail(user.getEmail());

							String data = String.format(Constant.NOTIFY_SOLICITUDE_STEP_5_FINISH,
									solicitude.getDataProcess().getProcessName());

							if (!solicitude.getBudgetStructure().isAvailabilityStructure()) {
								data = data + "\n" + String.format(Constant.NOTIFY_SOLICITUDE_STEP_5_OPT_FINISH,
										solicitude.getBudgetStructure().getAdditionalComment());
							}

							request.setObs(data);

							request.setTemplate(Constant.TYPE_TEMPLATE);
							request.setSubject(Constant.NOTIFY_SUBJET);

							String url = config.getConfiguration().getUrlLogin() + Constant.URL_HOME_PROCESS ;
							
							request.setDetailsURL(url);

							notifyClient.sendUpdateNotify(request);
						}
					}
				});
			});
		}

	}

	@Override
	public Mono<SolicitudeItemResponse> accreditSolicitude(String idProject, ApproveSolicitudeRequest request,
			UserSession userSession) {

		return projectDao.findById(idProject).flatMap(project -> {

			if (!Strings.isEmpty(project.getIdAccredited())) {
				throw new GlobalException(HttpStatus.BAD_REQUEST,
						TypeStatusCode.MESSAGE_ERROR_BLOCKCHAIN_REQUEST_ACCREDITED.name());
			}

			return userDao.findByEmail(userSession.getEmail()).flatMap(user -> {

				return blockChain.hasRoleUser(userSession.getRole(), user.getProxyAddress()).flatMap(flag -> {

					if (flag) {
						return solicitudeDao
								.accreditSolicitude(request.getIdSolicitude(), request, userSession.getRole(), user)
								.flatMap(item -> {
									sendEmailAccredited(userSession, item);
									SolicitudeItemResponse destiny = new SolicitudeItemResponse();

									new SolicitudeMap().mapping(item, destiny);
									createProcess(item, userSession);

									return Mono.just(destiny);
								});
					} else {
						throw new GlobalException(HttpStatus.BAD_REQUEST,
								TypeStatusCode.MESSAGE_USER_NOT_ROLE.name() + ":" + userSession.getRole());
					}

				});

			});
		});
	}

	@Override
	public Mono<SolicitudeItemResponse> notAccredited(ApproveSolicitudeRequest request, UserSession userSession) {

		return solicitudeDao.findById(request.getIdSolicitude()).flatMap(item -> {

			sendEmailNoAccredited(userSession, item, request.getObservation());

			SolicitudeItemResponse destiny = new SolicitudeItemResponse();

			new SolicitudeMap().mapping(item, destiny);

			/* RDELAROSA 06/06/2020 */
			updateProjectNotAccredited(item, userSession);
			/*******************************************/

			return Mono.just(destiny);
		});

	}

	private void sendEmailAccredited(UserSession userSession, Solicitude solicitude) {
		List<String> listRoles = new ArrayList<>();
		listRoles.add(Constants.ROLE_COO_TEC);
		listRoles.add(Constants.ROLE_ESP_ADQ);
		listRoles.add(Constants.ROLE_DIR_ADQ);
		listRoles.add(Constants.ROLE_DIR_ADJ);
		listRoles.add(Constants.ROLE_DIR_EJE);

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

						request.setObs(String.format(Constant.NOTIFY_SOLICITUDE_ACCREDITED,
								solicitude.getDataProcess().getProcessName()));
						request.setTemplate(Constant.TYPE_TEMPLATE);
						request.setSubject(Constant.NOTIFY_SUBJET);

						String url = config.getConfiguration().getUrlLogin() + Constant.URL_HOME_PROCESS;

						request.setDetailsURL(url);

						notifyClient.sendUpdateNotify(request);
					}
				}
			});

		});
	}

	private void sendEmailNoAccredited(UserSession userSession, Solicitude solicitude, String observation) {
		List<String> listRoles = new ArrayList<>();
		listRoles.add(Constants.ROLE_COO_TEC);

		userDao.findUserByRoles(listRoles).collectList().subscribe(list -> {

			list.forEach(user -> {
				log.info("codeStatus ..." + user.getCodeStatus());
				if (user.getCodeStatus().equals(Constant.USER_HABILITADO)) {

					if (!StringUtils.isEmpty(user.getEmail())) {
						log.info("sendEmailNoAccredited ..." + user.getEmail());

						ItemUserNotifyRequest request = new ItemUserNotifyRequest();
						request.setEmail(user.getEmail());
						request.setUserId(user.getId());
						request.setName(user.getFullname());

						request.setObs(String.format(Constant.NOTIFY_SOLICITUDE_NO_ACCREDITED,
								solicitude.getDataProcess().getProcessName(), observation));
						request.setTemplate(Constant.TYPE_TEMPLATE);
						request.setSubject(Constant.NOTIFY_SUBJET);
						
						String url = config.getConfiguration().getUrlLogin() + Constant.URL_HOME_PROCESS;

						request.setDetailsURL(url);

						notifyClient.sendUpdateNotify(request);
					}
				}
			});

		});
	}

	private void createProcess(Solicitude solicitude, UserSession userSession) {
		Process process = new Process();
		new ProcessMap().mappingCreate(solicitude.getIdProject(), process, userSession);
		processDao.createProcess(process).subscribe(item -> {
			updateProjectAccredited(solicitude, item, userSession);
		});

	}

	private Mono<Project> saveProject(Solicitude solicitude, UserSession userSession) {
		Project project = new Project();
		new ProjectMap().mappingCreate(solicitude, project, userSession);
		return projectDao.create(project, userSession).map(item -> {
			solicitude.setIdProject(item.getId());
			solicitudeDao.updateSolicitude(solicitude.getId(), item.getId()).subscribe();

			return item;
		});
	}

	private void updateProject(Solicitude solicitude, UserSession userSession) {
		Project project = new Project();

		new ProjectMap().finishSolicitude(project, solicitude);

		projectDao.update(solicitude.getIdProject(), project, userSession).subscribe();
	}

	private void updateProjectAccredited(Solicitude solicitude, Process process, UserSession userSession) {
		Project project = new Project();

		new ProjectMap().accreditedSolicitude(project, solicitude, process);
		projectDao.update(solicitude.getIdProject(), project, userSession).subscribe();
	}

	private void updateProjectNotAccredited(Solicitude solicitude, UserSession userSession) {
		Project project = new Project();

		new ProjectMap().notAccreditedSolicitude(project, solicitude);
		projectDao.update(solicitude.getIdProject(), project, userSession).subscribe();
	}

}
