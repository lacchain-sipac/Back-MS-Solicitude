package com.invest.honduras.dao;

import com.invest.honduras.domain.model.Project;
import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.domain.model.User;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.process.FlowFile;
import com.invest.honduras.domain.model.process.Process;
import com.invest.honduras.http.client.process.ApproveRequest;
import com.invest.honduras.http.client.process.CommitteeRequest;
import com.invest.honduras.http.client.process.CommitteeUpdateRequest;
import com.invest.honduras.http.client.process.ProcessRequest;

import reactor.core.publisher.Mono;

public interface ProcessDao {

	Mono<Process> findByIdProject(String id);

	Mono<Process> findById(String id);

	Mono<Process> initProcess(Project project, String codeStep, String role, User user);

	Mono<Process> createProcess(Process process);

	Mono<Process> updateProcess(String id, ProcessRequest request, UserSession userSession);

	Mono<Process> updateProcessStorage(String idProcess, FlowFile flowFile, Storage source, User user);

	Mono<Process> updateProcessCommittee(String idProcess, CommitteeUpdateRequest request, UserSession userSession);

	Mono<Process> addProcessCommittee(String idProcess, CommitteeRequest request, UserSession userSession);

	Mono<Process> deleteProcessCommittee(String idProcess, String idCommittee, UserSession userSession);

	Mono<Process> updateProcessApproved(String idProcess, String hash, ApproveRequest request, User userSession,
			boolean flag);

}
