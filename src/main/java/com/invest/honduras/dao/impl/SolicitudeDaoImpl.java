package com.invest.honduras.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.invest.honduras.blockchain.StepManagerBlockChain;
import com.invest.honduras.config.ConfigurationProject;
import com.invest.honduras.dao.SolicitudeDao;
import com.invest.honduras.domain.map.SolicitudeMap;
import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.domain.model.User;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.solicitude.ApproveSolicitudeRequest;
import com.invest.honduras.domain.model.solicitude.Solicitude;
import com.invest.honduras.domain.model.vc.CredentialSubjectMap;
import com.invest.honduras.enums.TypeStatusCode;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.http.client.solicitude.SolicitudeUpdateRequest;
import com.invest.honduras.repository.SolicitudeRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SolicitudeDaoImpl implements SolicitudeDao {

	@Autowired
	SolicitudeRepository solicitudeRepository;

	@Autowired
	StepManagerBlockChain stepManager;

	@Autowired
	ConfigurationProject app;

	@Autowired
	StepManagerBlockChain blockChain;

	public Mono<Solicitude> findById(String id) {
		return solicitudeRepository.findById(id);
	}

	@Override
	public Mono<Solicitude> createSolicitude(Solicitude solicitude) {
		return solicitudeRepository.insert(solicitude);
	}

	@Override
	public Flux<Solicitude> findAllSolicitude() {

		return solicitudeRepository.findAll(Sort.by("lastModifiedDate").descending());
	}

	@Override
	public Mono<Solicitude> updateSolicitude(String id, String idProject) {
		return findById(id).doOnSuccess(findItem -> {

			if (findItem != null) {
				findItem.setIdProject(idProject);
				solicitudeRepository.save(findItem).subscribe();

			}
		});
	}

	@Override
	public Mono<Solicitude> updateSolicitude(String id, SolicitudeUpdateRequest solicitudeUpdateRequest,
			UserSession userSession) {
		return findById(id).doOnSuccess(findItem -> {

			if (findItem != null) {

				new SolicitudeMap().mapping(solicitudeUpdateRequest, findItem, userSession);

				solicitudeRepository.save(findItem).subscribe();

			}
		}).switchIfEmpty(Mono.error(
				new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.SOLICITUDE_ID_NOT_FOUND.name() + ":" + id)));

	}

	@Override
	public Mono<Solicitude> updateSolicitudeStorage(String id, Storage storage, UserSession userSession) {
		return findById(id).doOnSuccess(findItem -> {

			if (findItem != null) {

				new SolicitudeMap().mappingInsert(storage, findItem, userSession);

				solicitudeRepository.save(findItem).subscribe();

			}
		}).switchIfEmpty(Mono.error(
				new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.SOLICITUDE_ID_NOT_FOUND.name() + ":" + id)));

	}

	@Override
	public Mono<Solicitude> deleteSolicitudeStorage(String id, String idStorage, UserSession userSession) {
		return findById(id).doOnSuccess(findItem -> {

			if (findItem != null) {

				new SolicitudeMap().mappingDelete(idStorage, findItem, userSession);

				solicitudeRepository.save(findItem).subscribe();

			}
		}).switchIfEmpty(Mono.error(
				new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.SOLICITUDE_ID_NOT_FOUND.name() + ":" + id)));

	}

	@Override
	public Mono<Solicitude> accreditSolicitude(String id, ApproveSolicitudeRequest request, String role, User user) {
		return findById(id).flatMap(findItem -> {

			new CredentialSubjectMap().mappingAccredited(findItem, role, user, request.getObservation());
			new CredentialSubjectMap().calculateHash(findItem, app.getConfiguration());

			return blockChain.setInitBlockchain(findItem.getAccredited().getHash(), user.getProxyAddress(), role,
					request.getCodeStep()).flatMap(response -> {
						if (HttpStatus.OK.value() == response.getStatus()) {

							solicitudeRepository.save(findItem).subscribe();
							return Mono.just(findItem);
						} else {
							throw new GlobalException(HttpStatus.BAD_REQUEST, response.getError());
						}

					});

		}).switchIfEmpty(Mono.error(
				new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.SOLICITUDE_ID_NOT_FOUND.name() + ":" + id)));

	}

}
