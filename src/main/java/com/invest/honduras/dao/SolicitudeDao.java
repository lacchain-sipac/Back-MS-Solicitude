package com.invest.honduras.dao;

import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.domain.model.User;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.solicitude.ApproveSolicitudeRequest;
import com.invest.honduras.domain.model.solicitude.Solicitude;
import com.invest.honduras.http.client.solicitude.SolicitudeUpdateRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolicitudeDao {

	Mono<Solicitude> findById(String id);

	Mono<Solicitude> createSolicitude(Solicitude solicitude);

	Flux<Solicitude> findAllSolicitude();

	Mono<Solicitude> updateSolicitude(String id, String idProject);

	Mono<Solicitude> updateSolicitude(String id, SolicitudeUpdateRequest solicitudeUpdateRequest,
			UserSession userSession);

	Mono<Solicitude> accreditSolicitude(String id, ApproveSolicitudeRequest request, String role, User user);

	Mono<Solicitude> updateSolicitudeStorage(String id, Storage storage, UserSession userSession);

	Mono<Solicitude> deleteSolicitudeStorage(String id, String idStorage, UserSession userSession);

}
