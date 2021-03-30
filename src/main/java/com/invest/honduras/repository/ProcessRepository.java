package com.invest.honduras.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.invest.honduras.domain.model.process.Process;

import reactor.core.publisher.Mono;

public interface ProcessRepository extends ReactiveMongoRepository<Process, String> {


	Mono<Process> findByIdProject(String idProject);
}
