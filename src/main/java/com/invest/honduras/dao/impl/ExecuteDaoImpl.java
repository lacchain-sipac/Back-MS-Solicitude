package com.invest.honduras.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.invest.honduras.dao.ExecuteDao;
import com.invest.honduras.domain.map.ExecuteApprovedMap;
import com.invest.honduras.domain.map.ExecuteFileMap;
import com.invest.honduras.domain.map.ExecuteMap;
import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.domain.model.User;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.execute.Execute;
import com.invest.honduras.domain.model.process.FlowFile;
import com.invest.honduras.enums.TypeStatusCode;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.http.client.execute.ExecuteRequest;
import com.invest.honduras.http.client.process.ApproveRequest;
import com.invest.honduras.repository.ExecuteRepository;

import reactor.core.publisher.Mono;

@Component
public class ExecuteDaoImpl implements ExecuteDao {

	@Autowired
	ExecuteRepository repository;

	public Mono<Execute> findByIdProject(String idProject) {
		return repository.findByIdProject(idProject);

	}

	public Mono<Execute> findById(String id) {
		return repository.findById(id);

	}

	@Override
	public Mono<Execute> createExecute(Execute execute) {
		return repository.insert(execute);
	}

	@Override
	public Mono<Execute> updateExecute(String id, ExecuteRequest request, UserSession userSession) {
		return repository.findById(id).doOnSuccess(findItem -> {

			if (findItem != null) {

				new ExecuteMap().mapping(request, findItem, userSession);

				repository.save(findItem).subscribe();

			} 
		}).switchIfEmpty(Mono.error(
				new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.EXECUTE_ID_NOT_FOUND.name() + ":" + id)));
	}

	@Override
	public Mono<Execute> updateExecuteStorage(FlowFile flowFile, String idExecute, Storage storage, User user) {
		return repository.findById(idExecute).doOnSuccess(findItem -> {

			if (findItem != null) {

				new ExecuteFileMap().mappingFileInsert(findItem, flowFile, storage, user);

				repository.save(findItem).subscribe();

			} 
		}).switchIfEmpty(Mono.error(
				new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.EXECUTE_ID_NOT_FOUND.name() + ":" + idExecute)));
	}

	@Override
	public Mono<Execute> updateExecuteApproved(String idExecute, String hash, ApproveRequest request, User userSession,
			boolean accredited) {
		return repository.findById(idExecute).doOnSuccess(findItem -> {

			if (findItem != null) {

				new ExecuteApprovedMap().mapping(request, hash, findItem, userSession, accredited);

				repository.save(findItem).subscribe();

			}
		}).switchIfEmpty(Mono.error(
				new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.EXECUTE_ID_NOT_FOUND.name() + ":" + idExecute)));
	}

}
