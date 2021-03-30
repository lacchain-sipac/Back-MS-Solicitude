package com.invest.honduras.dao.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;

import com.invest.honduras.config.PropertiesStorage;
import com.invest.honduras.dao.StorageDao;
import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.enums.TypeStatusCode;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.repository.StorageRepository;
import com.invest.honduras.util.FileUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class StorageDaoImpl implements StorageDao {

	@Autowired
	StorageRepository repository;

	@Autowired
	PropertiesStorage prop;

	@Override
	public Mono<Storage> findById(String id) {
		return repository.findById(id).map(item -> {
			try {
				Resource re = FileUtil.getFile(item.getFileName(), id, prop.getVolumenPath());
				log.info("StorageDaoImpl.findById {} {}", id, re);
				item.setFile(re);

			} catch (IOException e) {
				log.error("Error.StorageDaoImpl.findById", e);
			}

			return item;
		}).switchIfEmpty(Mono.error(
				new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.FILE_NOT_EXISTS.name() + ":" + id)));

	}

	@Override
	public Mono<Storage> saveStorage(Storage storage, FilePart file) {
		storage.setId(UUID.randomUUID().toString().replaceAll("-", "_"));
		storage.setFile(null);

		return FileUtil.saveFiles(storage, prop.getVolumenPath(), file).flatMap(rs -> {
			
			return repository.insert(storage).flatMap(mapper -> {

				return Mono.just(mapper);
			});
		});

		/* QUIT SAVE FOREVER IN MONGO RDELAROSA 11/06/2020 */
		/*
		 * if(save) { return repository.insert(storage).flatMap(mapper -> {
		 * mapper.setFile(null); return Mono.just(mapper); }); }else {
		 * storage.setId(UUID.randomUUID().toString()); storage.setFile(null); return
		 * Mono.just(storage); }
		 */

		/* ADD FILE SAVE FOREVER IN MONGO RDELAROSA 11/06/2020 */

	}

	@Override
	public Mono<Boolean> delete(String id) {
		return repository.deleteById(id).thenReturn(true);

	}
}
