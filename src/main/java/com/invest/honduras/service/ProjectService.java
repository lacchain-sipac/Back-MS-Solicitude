package com.invest.honduras.service;

import java.util.List;

import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.http.client.ProjectFinishRequest;
import com.invest.honduras.http.response.ProjectItemResponse;

import reactor.core.publisher.Mono;

public interface ProjectService {

	Mono<Boolean> existsProject(String id);
	
	Mono<List<ProjectItemResponse>> findAllProject();

	Mono<ProjectItemResponse> findById(String id);

	Mono<ProjectItemResponse> financiaClosure(String id, ProjectFinishRequest request, UserSession userSession);
}
