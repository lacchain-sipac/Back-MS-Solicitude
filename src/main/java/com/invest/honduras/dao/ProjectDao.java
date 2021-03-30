package com.invest.honduras.dao;

import com.invest.honduras.domain.model.Project;
import com.invest.honduras.domain.model.UserSession;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProjectDao {

	Mono<Project> findById(String id);

	Flux<Project> findAll();

	Mono<Project> create(Project project, UserSession userSession);

	Mono<Project> update(String id, Project project, UserSession userSession);

}
