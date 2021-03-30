package com.invest.honduras.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.invest.honduras.blockchain.StepManagerBlockChain;
import com.invest.honduras.dao.ProcessDao;
import com.invest.honduras.domain.map.ProcessApprovedMap;
import com.invest.honduras.domain.map.ProcessFileWriteMap;
import com.invest.honduras.domain.map.ProcessMap;
import com.invest.honduras.domain.model.Project;
import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.domain.model.User;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.process.FlowFile;
import com.invest.honduras.domain.model.process.Process;
import com.invest.honduras.enums.TypeStatusCode;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.http.client.process.ApproveRequest;
import com.invest.honduras.http.client.process.CommitteeRequest;
import com.invest.honduras.http.client.process.CommitteeUpdateRequest;
import com.invest.honduras.http.client.process.ProcessRequest;
import com.invest.honduras.repository.ProcessRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ProcessDaoImpl implements ProcessDao {

	@Autowired
	ProcessRepository repository;

	@Autowired
	StepManagerBlockChain blockchain;

	@Override
	public Mono<Process> findByIdProject(String idProject) {
		return repository.findByIdProject(idProject);

	}

	@Override
	public Mono<Process> findById(String id) {
		return repository.findById(id);

	}

	@Override
	public Mono<Process> initProcess(Project project, String codeStep, String role, User user) {
		return repository.findById(project.getProcess().getId()).flatMap(process -> {

			if (process.isInitialized()) {
				throw new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.PROCESS_ID_ALREADY.name());
			}

			return blockchain.setChangeStepBlockchain(project.getIdAccredited(), user.getProxyAddress(), role, codeStep)
					.map(response -> {
						if (HttpStatus.OK.value() == response.getStatus()) {
							new ProcessMap().initProcess(process, user, codeStep);
							repository.save(process).subscribe();
							return process;
						} else {
							throw new GlobalException(HttpStatus.BAD_REQUEST, response.getError());
						}
					});
		});
	}

	@Override
	public Mono<Process> createProcess(Process process) {
		return repository.insert(process);
	}

	@Override
	public Mono<Process> updateProcess(String id, ProcessRequest request, UserSession userSession) {
		return repository.findById(id).doOnSuccess(findItem -> {

			if (findItem != null) {

				new ProcessMap().mapping(request, findItem, userSession);

				repository.save(findItem).subscribe();

			}
		}).switchIfEmpty(Mono.error(
				new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.PROCESS_ID_NOT_FOUND.name() + ":" + id)));
	}

	@Override
	public Mono<Process> updateProcessStorage(String idProcess, FlowFile flowFile, Storage source, User user) {

		return repository.findById(idProcess).doOnSuccess(findItem -> {

			if (findItem != null) {

				new ProcessFileWriteMap().mappingFileInsert(findItem, flowFile, source, user);

				repository.save(findItem).subscribe();
 

			} 
		}).switchIfEmpty(Mono.error(
				new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.PROCESS_ID_NOT_FOUND.name() + ":" + idProcess)));
		
	}

	@Override
	public Mono<Process> updateProcessCommittee(String idProcess, CommitteeUpdateRequest request,
			UserSession userSession) {
		return repository.findById(idProcess).doOnSuccess(findItem -> {

			if (findItem != null) {

				new ProcessMap().mappingUpdateCommittee(request, findItem, userSession);

				repository.save(findItem).subscribe();

			}  
		}).switchIfEmpty(Mono.error(
				new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.PROCESS_ID_NOT_FOUND.name() + ":" + idProcess)));
		
	}

	@Override
	public Mono<Process> addProcessCommittee(String idProcess, CommitteeRequest request, UserSession userSession) {
		return repository.findById(idProcess).doOnSuccess(findItem -> {

			if (findItem != null) {

				new ProcessMap().mappingAddCommittee(request, findItem, userSession);

				repository.save(findItem).subscribe();

			} 
		}).switchIfEmpty(Mono.error(
				new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.PROCESS_ID_NOT_FOUND.name() + ":" + idProcess)));
		
	}

	@Override
	public Mono<Process> deleteProcessCommittee(String idProcess, String idCommittee, UserSession userSession) {
		return repository.findById(idProcess).doOnSuccess(findItem -> {

			if (findItem != null) {

				new ProcessMap().mappingDeleteCommittee(idCommittee, findItem, userSession);

				repository.save(findItem).subscribe();

			}
		}).switchIfEmpty(Mono.error(
				new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.PROCESS_ID_NOT_FOUND.name() + ":" + idProcess)));
		
	}

	@Override
	public Mono<Process> updateProcessApproved(String idProcess, String hash, ApproveRequest request, User user,
			boolean isAccredited) {

		log.info("ProccessDAOImpl : + updaetPRocessApprove");

		return repository.findById(idProcess).doOnSuccess(findItem -> {

			if (findItem != null) {

				log.info("ProccessDAOImpl : + updaetPRocessApprove antes del mapping");

				new ProcessApprovedMap().mapping(request, hash, findItem, user, isAccredited);

				repository.save(findItem).subscribe();

			} 
		}).switchIfEmpty(Mono.error(
				new GlobalException(HttpStatus.BAD_REQUEST, TypeStatusCode.PROCESS_ID_NOT_FOUND.name() + ":" + idProcess)));
		
	}

	/*
	 * @Override public Mono<Process> updateProcessAccredited(String idProcess,
	 * ApproveRequest request, UserSession userSession) { return
	 * repository.findById(idProcess).doOnSuccess(findItem -> {
	 * 
	 * if (findItem != null) {
	 * 
	 * new ProcessAcreditedMap().mapping(request, "hash", findItem, userSession);
	 * 
	 * repository.save(findItem).subscribe();
	 * 
	 * } else { throw new GeneralRuntimeException(HttpStatus.OK,
	 * TypeStatusCode.SOLICITUDE_ID_NOT_FOUND.getCode(),
	 * TypeStatusCode.SOLICITUDE_ID_NOT_FOUND.getMessage()); } }); }
	 */

}
