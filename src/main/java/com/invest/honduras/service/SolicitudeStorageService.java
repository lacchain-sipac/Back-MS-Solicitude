package com.invest.honduras.service;

import java.util.List;

import org.springframework.http.codec.multipart.Part;

import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.http.response.solicitude.SolicitudeItemResponse;

import reactor.core.publisher.Mono;
public interface SolicitudeStorageService {

	Mono<Storage> findById(String id);

	Mono<SolicitudeItemResponse> create(String idSolicitude, List<Part> part, UserSession userSession);

	Mono<SolicitudeItemResponse> delete(String idSolicitude, String id, UserSession userSession);

}
