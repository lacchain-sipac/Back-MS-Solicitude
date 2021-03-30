package com.invest.honduras.service;

import java.util.List;

import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.http.client.ExecuteCreateRequest;
import com.invest.honduras.http.client.execute.ExecuteRequest;
import com.invest.honduras.http.client.process.ApproveRequest;
import com.invest.honduras.http.response.UserItemResponse;
import com.invest.honduras.http.response.execution.ExecuteItemResponse;

import reactor.core.publisher.Mono;

public interface ExecuteService {

	Mono<ExecuteItemResponse> findById(String id);

	Mono<ExecuteItemResponse> findByIdProject(String idProject);

	Mono<ExecuteItemResponse> createExecute(String idProject, ExecuteCreateRequest request, UserSession userSession);

	Mono<ExecuteItemResponse> updateExecute(String id, ExecuteRequest request, UserSession userSession);

	Mono<List<UserItemResponse>> getUsersByRole(String role);

	Mono<ExecuteItemResponse> approveDocument(String idExecute, ApproveRequest request, UserSession userSession, boolean flag);


	Mono<Boolean> existsDocument(String idProject, String idProcess, String typeDocument, String hash);

	Mono<Boolean> existsComment(String idProject, String idProcess, String typeDocument, String hash);
}
