package com.invest.honduras.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.invest.honduras.dao.ProjectDao;
import com.invest.honduras.domain.map.ProjectMap;
import com.invest.honduras.domain.model.Project;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.enums.TypeStatusCode;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.repository.ProjectRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProjectDaoImpl implements ProjectDao {

	@Autowired
	ProjectRepository repository;

	@Override
	public Mono<Project> findById(String id) {
		return repository.findById(id);
	}

	@Override
	public Mono<Project> create(Project project, UserSession userSession) {
		return repository.insert(project);
	}

	@Override
	public Flux<Project> findAll() {

		return repository.findAll(Sort.by("lastModifiedDate").descending());
	}

	@Override
	public Mono<Project> update(String id, Project project, UserSession userSession) {
		return findById(id).doOnSuccess(item -> {

			if (item != null) {
				new ProjectMap().mappingUpdate(project, item, userSession);
				repository.save(item).subscribe();

			}
		}).switchIfEmpty(Mono.error(
				new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.PROJECT_NOT_EXISTS.name() + ":" + id)));

	}

}
