package com.invest.honduras.service;

import java.util.List;

import org.springframework.http.codec.multipart.Part;

import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.http.response.process.ProcessItemResponse;

import reactor.core.publisher.Mono;
public interface ProcessStorageService {

	Mono<Storage> findById(String id);

	Mono<ProcessItemResponse> create(String idProcess, List<Part> part, UserSession userSession);


}
