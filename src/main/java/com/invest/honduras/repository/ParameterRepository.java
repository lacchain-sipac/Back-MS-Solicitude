package com.invest.honduras.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.invest.honduras.domain.model.solicitude.Parameter;

public interface ParameterRepository extends MongoRepository<Parameter, String> {

}
