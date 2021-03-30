package com.invest.honduras.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.invest.honduras.domain.model.Configuration;

public interface ConfigurationRepository extends MongoRepository<Configuration, String> {

}
