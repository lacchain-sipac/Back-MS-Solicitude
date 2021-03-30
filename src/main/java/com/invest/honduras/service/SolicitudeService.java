package com.invest.honduras.service;

import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.solicitude.ApproveSolicitudeRequest;
import com.invest.honduras.http.client.solicitude.SolicitudeUpdateRequest;
import com.invest.honduras.http.response.solicitude.SolicitudeItemResponse;

import reactor.core.publisher.Mono;

public interface SolicitudeService {

	Mono<SolicitudeItemResponse> createSolicitude(UserSession userSession);

	Mono<SolicitudeItemResponse> findById(String id);

	Mono<SolicitudeItemResponse> updateSolicitude(String id, SolicitudeUpdateRequest solicitudeUpdateRequest,
			UserSession userSession);

	Mono<SolicitudeItemResponse> accreditSolicitude(String idProject, ApproveSolicitudeRequest request,
			UserSession userSession);
	
	Mono<SolicitudeItemResponse> notAccredited(ApproveSolicitudeRequest request, UserSession userSession);

}
