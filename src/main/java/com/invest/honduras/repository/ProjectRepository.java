package com.invest.honduras.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.invest.honduras.domain.model.Project;

public interface ProjectRepository extends ReactiveMongoRepository<Project, String> {

}
