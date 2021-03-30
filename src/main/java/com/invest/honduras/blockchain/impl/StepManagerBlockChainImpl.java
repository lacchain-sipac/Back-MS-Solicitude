package com.invest.honduras.blockchain.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.invest.honduras.blockchain.StepManagerBlockChain;
import com.invest.honduras.blockchain.client.BlockChainClient;
import com.invest.honduras.dao.ProjectDao;
import com.invest.honduras.dao.UserDao;
import com.invest.honduras.domain.model.Project;
import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.process.FlowFile;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.http.client.DocumentStepRequest;
import com.invest.honduras.http.client.StepRequest;
import com.invest.honduras.http.client.UserRoleProjectRequest;
import com.invest.honduras.http.client.process.ApproveRequest;
import com.invest.honduras.http.response.DocumentItem;
import com.invest.honduras.http.response.ProjectItem;
import com.invest.honduras.http.response.ResponseGeneral;

import reactor.core.publisher.Mono;

@Component
public class StepManagerBlockChainImpl implements StepManagerBlockChain {

	@Autowired
	UserDao userDao;

	@Autowired
	ProjectDao projectDao;

	@Autowired
	BlockChainClient blockchain;

	@Override
	public Mono<ResponseGeneral> approveDocumentBlockchain(ApproveRequest request, String idProcess,
			UserSession userSession, String hash) {
		return projectDao.findById(request.getIdProject()).flatMap(project -> {
			return userDao.findByEmail(userSession.getEmail()).flatMap(user -> {
				return callApproveDocumentBlockchain(project.getIdAccredited(), user.getProxyAddress(),
						userSession.getRole(), request.getCodeStep(), request.getDocumentType(), hash);
			});
		});
	}

	private Mono<ResponseGeneral> callApproveDocumentBlockchain(String projectCode, String proxyAddress, String role,
			String step, String documentType, String commentHash) {
		DocumentStepRequest request = new DocumentStepRequest();
		request.setProjectCode(projectCode);
		request.setProxyAddress(proxyAddress);
		request.setRole(role);
		request.setStep(step);
		request.setDocumentType(documentType);
		request.setDocumentHash("");
		request.setCommentHash(commentHash);
		request.setFinish(false);

		return blockchain.addDocument(request);
	}

	@Override
	public Mono<ResponseGeneral> setInitBlockchain(String projectCode, String proxyAddress, String role, String step) {
		StepRequest request = new StepRequest();
		request.setProjectCode(projectCode);
		request.setProxyAddress(proxyAddress);
		request.setRole(role);
		request.setStep(step);

		return blockchain.initProject(request);
	}

	@Override
	public Mono<ResponseGeneral> setChangeStepBlockchain(String projectCode, String proxyAddress, String role,
			String step) {

		StepRequest request = new StepRequest();
		request.setProjectCode(projectCode);
		request.setProxyAddress(proxyAddress);
		request.setRole(role);
		request.setStep(step);

		return blockchain.changeStep(request);
	}

	@Override
	public Mono<ResponseGeneral> addDocumentBlockchain(FlowFile flowFile, Storage storage, UserSession userSession) {
		return projectDao.findById(flowFile.getIdProject()).flatMap(project -> {
			return userDao.findByEmail(userSession.getEmail()).flatMap(user -> {
				return callAddDocumentBlockchain(project.getIdAccredited(), user.getProxyAddress(),
						userSession.getRole(), flowFile.getCodeStep(), flowFile.getDocumentType(), storage.getHash(),
						flowFile.isAccredited());
			});
		});
	}

	private Mono<ResponseGeneral> callAddDocumentBlockchain(String projectCode, String proxyAddress, String role,
			String step, String documentType, String documentHash, boolean finish) {
		DocumentStepRequest request = new DocumentStepRequest();
		request.setProjectCode(projectCode);
		request.setProxyAddress(proxyAddress);
		request.setRole(role);
		request.setStep(step);
		request.setDocumentType(documentType);
		request.setDocumentHash(documentHash);
		request.setCommentHash("");
		request.setFinish(finish);

		return blockchain.addDocument(request);
	}

	@Override
	public void addUserRoleProject(String projectCode, String proxyAddressUser, String role,
			String proxyAddressUserSession) {
		UserRoleProjectRequest request = new UserRoleProjectRequest();
		request.setProjectCodeHash(projectCode);
		request.setProxyAddressUserSession(proxyAddressUserSession);
		request.setRole(role);
		request.setUser(proxyAddressUser);
		blockchain.addUserRoleProject(request);
	}

	@Override
	public void removeUserRoleProject(String projectCode, String proxyAddressUser, String role,
			String proxyAddressUserSession) {
		UserRoleProjectRequest request = new UserRoleProjectRequest();
		request.setProjectCodeHash(projectCode);
		request.setProxyAddressUserSession(proxyAddressUserSession);
		request.setRole(role);
		request.setUser(proxyAddressUser);
		blockchain.removeUserRoleProject(request);
	}

	@Override
	public Mono<Boolean> hasRoleUser(String role, String user) {
		return blockchain.hasRoleUser(role, user).map(response -> {
			if (HttpStatus.OK.value() == response.getStatus()) {

				return response.getData().isResult();
			} else {
				throw new GlobalException(HttpStatus.BAD_REQUEST, response.getError());
			}

		});
	}

	@Override
	public Mono<Boolean> hasRoleUserProject(String role, String user, Project project) {
		return blockchain.hasRoleUserProject(role, user, project.getIdAccredited()).map(response -> {
			if (HttpStatus.OK.value() == response.getStatus()) {

				return response.getData().isResult();
			} else {
				throw new GlobalException(HttpStatus.BAD_REQUEST, response.getError());
			}

		});
	}

	@Override
	public Mono<ProjectItem> getProject(String hashProject) {
		return blockchain.getProject(hashProject).map(response -> {
			if (HttpStatus.OK.value() == response.getStatus()) {

				return response.getData();
			} else {
				throw new GlobalException(HttpStatus.BAD_REQUEST, response.getError());
			}

		});
	}

	@Override
	public Mono<DocumentItem> getDocument(String hashProject, String typeDocument, String hash) {
		return blockchain.getDocument(hashProject, typeDocument, hash).map(response -> {
			if (HttpStatus.OK.value() == response.getStatus()) {

				return response.getData();
			} else {
				throw new GlobalException(HttpStatus.BAD_REQUEST, response.getError());
			}

		});
	}

	@Override
	public Mono<DocumentItem> getComment(String hashProject, String typeDocument, String hash) {
		return blockchain.getComment(hashProject, typeDocument, hash).map(response -> {
			if (HttpStatus.OK.value() == response.getStatus()) {

				return response.getData();
			} else {
				throw new GlobalException(HttpStatus.BAD_REQUEST, response.getError());
			}

		});
	}

}
