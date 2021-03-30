package com.invest.honduras.service;

import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.http.client.ProcessInitRequest;
import com.invest.honduras.http.client.process.ApproveRequest;
import com.invest.honduras.http.client.process.CommitteeDeleteRequest;
import com.invest.honduras.http.client.process.CommitteeRequest;
import com.invest.honduras.http.client.process.CommitteeUpdateRequest;
import com.invest.honduras.http.client.process.ProcessRequest;
import com.invest.honduras.http.response.process.ProcessItemResponse;

import reactor.core.publisher.Mono;

public interface ProcessService {

	Mono<ProcessItemResponse> findById(String id);

	Mono<ProcessItemResponse> findByIdProject(String idProject);

	Mono<ProcessItemResponse> initProcess(String id, ProcessInitRequest request, UserSession userSession);

	Mono<ProcessItemResponse> updateProcess(String id, ProcessRequest request, UserSession userSession);

	Mono<ProcessItemResponse> addUserCommittee(String idProcess, CommitteeRequest request, UserSession userSession);

	Mono<ProcessItemResponse> updateUserCommittee(String idProcess, CommitteeUpdateRequest request,
			UserSession userSession);

	Mono<ProcessItemResponse> deleteUserCommittee(CommitteeDeleteRequest request, UserSession userSession);

	Mono<ProcessItemResponse> approveDocument(String idProcess, ApproveRequest request, UserSession userSession,
			boolean flag);

//	Mono<ProcessItemResponse>  acreditedDocument(String idProcess, ApproveRequest request , UserSession userSession);

	Mono<Boolean> existsDocument(String idProject, String idProcess, String typeDocument, String hash);

	Mono<Boolean> existsComment(String idProject, String idProcess, String typeDocument, String hash);

}
