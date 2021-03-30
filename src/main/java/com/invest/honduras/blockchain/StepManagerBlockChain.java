package com.invest.honduras.blockchain;

import com.invest.honduras.domain.model.Project;
import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.process.FlowFile;
import com.invest.honduras.http.client.process.ApproveRequest;
import com.invest.honduras.http.response.DocumentItem;
import com.invest.honduras.http.response.ProjectItem;
import com.invest.honduras.http.response.ResponseGeneral;

import reactor.core.publisher.Mono;

public interface StepManagerBlockChain {

	Mono<ResponseGeneral> approveDocumentBlockchain(ApproveRequest request, String idProcess, UserSession userSession,
			String hash);

	Mono<ResponseGeneral> setInitBlockchain(String projectCode, String proxyAddress, String role, String step);

	Mono<ResponseGeneral> setChangeStepBlockchain(String projectCode, String proxyAddress, String role, String step);

	Mono<ResponseGeneral> addDocumentBlockchain(FlowFile flowFile, Storage storage, UserSession userSession);

	void addUserRoleProject(String projectCode, String proxyAddressUser, String role, String proxyAddressUserSession);

	void removeUserRoleProject(String projectCode, String proxyAddressUser, String role,
			String proxyAddressUserSession);

	Mono<Boolean> hasRoleUser(String role, String user);

	Mono<Boolean> hasRoleUserProject(String role, String user, Project project);

	Mono<DocumentItem> getComment(String hashProject, String typeDocument, String hash);

	Mono<DocumentItem> getDocument(String hashProject, String typeDocument, String hash);

	Mono<ProjectItem> getProject(String hashProject);
}
