package com.invest.honduras.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.invest.honduras.domain.model.project.Rule;

import reactor.core.publisher.Mono;

public interface RuleRepository  extends ReactiveMongoRepository<Rule, String>{
		 
	  Mono<Rule> findByCode(String code);
	   
}
