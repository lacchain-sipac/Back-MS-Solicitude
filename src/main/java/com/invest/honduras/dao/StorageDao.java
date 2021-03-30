package com.invest.honduras.dao;

import org.springframework.http.codec.multipart.FilePart;

import com.invest.honduras.domain.model.Storage;

import reactor.core.publisher.Mono;

public interface StorageDao {

	Mono<Storage> findById(String id);

	Mono<Storage> saveStorage(Storage storage, FilePart file);
	
	Mono<Boolean> delete(String id);


}
