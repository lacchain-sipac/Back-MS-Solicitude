package com.invest.honduras.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.invest.honduras.blockchain.StepManagerBlockChain;
import com.invest.honduras.config.ConfigurationProject;
import com.invest.honduras.dao.ParameterDao;
import com.invest.honduras.dao.ProjectDao;
import com.invest.honduras.dao.SolicitudeDao;
import com.invest.honduras.dao.UserDao;
import com.invest.honduras.domain.map.ProjectMap;
import com.invest.honduras.domain.model.Project;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.solicitude.StatusSolicitude;
import com.invest.honduras.domain.model.vc.CredentialSubjectMap;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.http.client.ProjectFinishRequest;
import com.invest.honduras.http.response.ProjectItemResponse;
import com.invest.honduras.notify.NotifyClient;
import com.invest.honduras.notify.request.ItemUserNotifyRequest;
import com.invest.honduras.service.ProjectService;
import com.invest.honduras.util.Constant;
import com.invest.honduras.util.Constants;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	ParameterDao parameterDao;

	@Autowired
	ProjectDao projectDao;

	@Autowired
	SolicitudeDao solicitudeDao;

	@Autowired
	UserDao userDao;

	@Autowired
	StepManagerBlockChain blockChain;
	@Autowired
	ConfigurationProject config;

	@Autowired
	NotifyClient notifyClient;

	public List<StatusSolicitude> listStatusSolicitude() {
		return parameterDao.listStatusSolicitude();
	}

	@Override
	public Mono<Boolean> existsProject(String id) {
		return projectDao.findById(id).flatMap(project -> {
			return solicitudeDao.findById(project.getSolicitude().getId()).flatMap(solicitude -> {

				new CredentialSubjectMap().calculateHash(solicitude, config.getConfiguration());

				return blockChain.getProject(solicitude.getAccredited().getHash()).flatMap(proy -> {
					return Mono.just(proy.isInitialized());
				});

			});
		});
	}

	@Override
	public Mono<List<ProjectItemResponse>> findAllProject() {

		final ProjectMap projectMap = new ProjectMap();

		List<StatusSolicitude> statusSolicitudes = listStatusSolicitude();

		return projectDao.findAll().collectList().map(listItem -> {

			List<ProjectItemResponse> listItemResponse = new ArrayList<>();

			listItem.forEach(item -> {

				ProjectItemResponse destiny = new ProjectItemResponse();
				projectMap.mapping(item, destiny, statusSolicitudes);

				listItemResponse.add(destiny);

			});

			return listItemResponse;

		});
	}

	@Override
	public Mono<ProjectItemResponse> findById(String id) {
		final ProjectMap projectMap = new ProjectMap();

		List<StatusSolicitude> statusSolicitudes = listStatusSolicitude();

		return projectDao.findById(id).map(item -> {

			ProjectItemResponse destiny = new ProjectItemResponse();
			projectMap.mapping(item, destiny, statusSolicitudes);

			return destiny;

		});
	}

	@Override
	public Mono<ProjectItemResponse> financiaClosure(String id, ProjectFinishRequest request, UserSession userSession) {
		return userDao.findByEmail(userSession.getEmail()).flatMap(user -> {

			return projectDao.findById(id).flatMap(project -> {
				return blockChain.setChangeStepBlockchain(project.getIdAccredited(), user.getProxyAddress(),
						userSession.getRole(), request.getCodeStep()).flatMap(response -> {
							if (HttpStatus.OK.value() == response.getStatus()) {
								return saveFinanciaClosure(id, userSession);
							} else {
								throw new GlobalException(HttpStatus.BAD_REQUEST, response.getError());
							}

						});
			});

		});
	}

	private Mono<ProjectItemResponse> saveFinanciaClosure(String id, UserSession userSession) {
		final ProjectMap projectMap = new ProjectMap();

		Project project = new Project();

		projectMap.mappingFinish(id, project, userSession);

		List<StatusSolicitude> statusSolicitudes = listStatusSolicitude();

		return projectDao.update(id, project, userSession).map(item -> {
			ProjectItemResponse destiny = new ProjectItemResponse();
			projectMap.mapping(item, destiny, statusSolicitudes);
			sendNotifyEmailfinish(item);

			return destiny;
		});
	}

	private void sendNotifyEmailfinish(Project project) {
		String nameProcess = project.getSolicitude().getDataProcess().getProcessName();

		String observation = String.format(Constant.NOTIFY_PROJECT_FINISH, nameProcess);

		List<String> listRoles = new ArrayList<>();

		listRoles.add(Constants.ROLE_COO_TEC);
		listRoles.add(Constants.ROLE_DIR_ADQ);
		listRoles.add(Constants.ROLE_DIR_ADJ);
		listRoles.add(Constants.ROLE_DIR_EJE);

		sendEmail(listRoles, observation);
	}

	private void sendEmail(List<String> listRoles, String mesage) {
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
						request.setDetailsURL(config.getConfiguration().getUrlLogin());

						notifyClient.sendUpdateNotify(request);
					}
				}
			});

		});
	}
}
