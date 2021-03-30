package com.invest.honduras.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.invest.honduras.domain.model.solicitude.Solicitude;

public interface SolicitudeRepository extends ReactiveMongoRepository<Solicitude, String> {

}
