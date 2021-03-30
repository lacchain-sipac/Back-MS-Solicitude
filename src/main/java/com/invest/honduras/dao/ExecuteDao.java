package com.invest.honduras.dao;

import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.domain.model.User;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.execute.Execute;
import com.invest.honduras.domain.model.process.FlowFile;
import com.invest.honduras.http.client.execute.ExecuteRequest;
import com.invest.honduras.http.client.process.ApproveRequest;

import reactor.core.publisher.Mono;

public interface ExecuteDao {

	Mono<Execute> findByIdProject(String id);

	Mono<Execute> findById(String id);

	Mono<Execute> createExecute(Execute process);

	Mono<Execute> updateExecute(String id, ExecuteRequest request, UserSession userSession);

	Mono<Execute> updateExecuteStorage(FlowFile flowFile, String idExecute, Storage storage, User user);

	Mono<Execute> updateExecuteApproved(String idExecute, String hash, ApproveRequest request, User userSession, boolean accredited);

}
