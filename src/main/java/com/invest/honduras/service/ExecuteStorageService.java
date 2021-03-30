package com.invest.honduras.service;

import java.util.List;

import org.springframework.http.codec.multipart.Part;

import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.http.response.execution.ExecuteItemResponse;

import reactor.core.publisher.Mono;
public interface ExecuteStorageService {

	Mono<Storage> findById(String id);

	Mono<ExecuteItemResponse> create(String idExecute, List<Part> part, UserSession userSession);


}
