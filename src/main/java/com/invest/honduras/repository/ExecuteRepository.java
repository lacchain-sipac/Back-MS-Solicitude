package com.invest.honduras.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.invest.honduras.domain.model.execute.Execute;

import reactor.core.publisher.Mono;

public interface ExecuteRepository extends ReactiveMongoRepository<Execute, String> {


	Mono<Execute> findByIdProject(String idProject);
}
