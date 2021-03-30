package com.invest.honduras.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.invest.honduras.domain.model.Storage;

public interface StorageRepository extends ReactiveMongoRepository<Storage, String> {

}
