package com.invest.honduras.blockchain.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.invest.honduras.dao.QueueDao;
import com.invest.honduras.domain.model.QueueTransaction;
import com.invest.honduras.domain.model.UserRoleProjectQueue;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.http.client.DocumentStepRequest;
import com.invest.honduras.http.client.StepRequest;
import com.invest.honduras.http.client.UserRoleProjectRequest;
import com.invest.honduras.http.response.ResponseGeneral;
import com.invest.honduras.http.response.ResponseGeneralBool;
import com.invest.honduras.http.response.ResponseGeneralDocument;
import com.invest.honduras.http.response.ResponseGeneralProject;
import com.invest.honduras.util.Constant;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
@Component
public class BlockChainClient {

	private final WebClient webClient;

	@Autowired
	QueueDao queueDao;
	
	public BlockChainClient() {
		this.webClient = WebClient.builder().baseUrl(Constant.HOST_BLOCKCHAIN)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

	public Mono<ResponseGeneral> initProject(StepRequest request) {
		
		log.info("BlockChainClient.initProject.post==" + Constant.HOST_BLOCKCHAIN + Constant.API_URL_BLOCKCHAIN_PROJECT);
		

		return this.webClient.post().uri(Constant.API_URL_BLOCKCHAIN_PROJECT)
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

				.body(Mono.just(request), StepRequest.class)
				.retrieve()
				.bodyToMono(ResponseGeneral.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
				.doOnError(e -> {
					log.error("doOnError initProject: " ,e);
					throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

				}).map(data -> {
					log.info("initProject--->>>>{}" , new Gson().toJson(data));
					return Mono.just(data);
				}).block();

	}

	public Mono<ResponseGeneral> changeStep(StepRequest request) {

		log.info("BlockChainClient.changeStep.put==" + Constant.HOST_BLOCKCHAIN + Constant.API_URL_BLOCKCHAIN_PROJECT);
		
		
		return this.webClient.put().uri(Constant.API_URL_BLOCKCHAIN_PROJECT).accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

				.body(Mono.just(request), StepRequest.class)
				.retrieve()
				.bodyToMono(ResponseGeneral.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
				.doOnError(e -> {
					log.error("doOnError:changeStep " , e);
					throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

				}).map(data -> {
					log.info("changeStep--->>>>{}" , new Gson().toJson(data));
					return Mono.just(data);
				}).block();

	}

	public Mono<ResponseGeneral> addDocument(DocumentStepRequest request) {

		log.info("BlockChainClient.addDocument.post==" + Constant.HOST_BLOCKCHAIN + Constant.API_URL_BLOCKCHAIN_DOCUMENT);
		
		
		return this.webClient.post().uri(Constant.API_URL_BLOCKCHAIN_DOCUMENT).accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

				.body(Mono.just(request), DocumentStepRequest.class)
				.retrieve()
				.bodyToMono(ResponseGeneral.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
				.doOnError(e -> {
					log.error("doOnError: addDocument " , e);
					throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

				}).map(data -> {
					log.info("addDocument--->>>>{}" , new Gson().toJson(data));
					return Mono.just(data);
				}).block();

	}
	

	public void removeUserRoleProject(UserRoleProjectRequest request) {
		
		log.info("BlockChainClient.removeUserRoleProject.put==" + Constant.HOST_BLOCKCHAIN + Constant.API_URL_BLOCKCHAIN_PROJECT_ROLE);
		
		
		this.webClient.put().uri(Constant.API_URL_BLOCKCHAIN_PROJECT_ROLE).accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(request), UserRoleProjectRequest.class)
				.retrieve()
				.bodyToMono(ResponseGeneral.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
				.doOnError(e -> {
					log.error("doOnError: ", e);
					throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
				}).subscribe(data -> {
					log.info("removeUserRoleProject-->>>> {}" , new Gson().toJson(data));
				});

	}
	
	public  Mono<ResponseGeneralBool> hasRoleUserProject(String role, String user, String project) {

		log.info("BlockChainClient.hasRoleUserProject.get==" + Constant.HOST_BLOCKCHAIN + Constant.API_URL_BLOCKCHAIN_HAS_USER_PROJECT_ROLE);
		
		return this.webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path(Constant.API_URL_BLOCKCHAIN_HAS_USER_PROJECT_ROLE)
						.queryParam("role", role)
					    .queryParam("proxy", user)
					    .queryParam("project", project)
						.build( ))
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

				.retrieve()
				.bodyToMono(ResponseGeneralBool.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
				
				.doOnError(e -> {
					log.error("doOnError:hasRoleUserProject " , e);
					throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

				}).map(data -> {
					log.info("hasRoleUserProject--->>>>{}" , new Gson().toJson(data));
					return Mono.just(data);
				}).block();

	}
	
	public Mono<ResponseGeneralBool> hasRoleUser(String role, String user) {
		log.info("BlockChainClient.hasRoleUser.get==" + Constant.HOST_BLOCKCHAIN + Constant.API_URL_BLOCKCHAIN_HAS_USER_ROLE);
		return this.webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path(Constant.API_URL_BLOCKCHAIN_HAS_USER_ROLE)
						.queryParam("role", role)
					    .queryParam("proxy", user)
						.build( ))
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

				.retrieve()
				.bodyToMono(ResponseGeneralBool.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
		
				.doOnError(e -> {
					log.error("doOnError:hasRoleUser " , e);
					throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

				}).map(data -> {
					log.info("hasRoleUser--->>>>{}" , new Gson().toJson(data));
					return Mono.just(data);
				}).block();

	}
	
	public Mono<ResponseGeneralProject> getProject(String hashProject) {
		log.info("BlockChainClient.getProject.get==" + Constant.HOST_BLOCKCHAIN + Constant.API_URL_BLOCKCHAIN_PROJECT);
		return this.webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path(Constant.API_URL_BLOCKCHAIN_PROJECT)
						.queryParam("hashProject", hashProject)
						.build( ))
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

				.retrieve()
				.bodyToMono(ResponseGeneralProject.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
				
				.doOnError(e -> {
					log.error("doOnError: getProject" , e);
					throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

				}).map(data -> {
					log.info("getProject--->>>>{}" , new Gson().toJson(data));
					return Mono.just(data);
				}).block();

	}
	
	public Mono<ResponseGeneralDocument> getDocument(String hashProject, String typeDocument, String hash) {
		log.info("BlockChainClient.getDocument.get==" + Constant.HOST_BLOCKCHAIN + Constant.API_URL_BLOCKCHAIN_DOCUMENT);
		return this.webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path(Constant.API_URL_BLOCKCHAIN_DOCUMENT)
						.queryParam("hashProject", hashProject)
						.queryParam("typeDocument", typeDocument)
						.queryParam("hash", hash)
						.build( ))
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

				.retrieve()
				.bodyToMono(ResponseGeneralDocument.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
				
				.doOnError(e -> {
					log.error("doOnError: getDocument  " , e);
					throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

				}).map(data -> {
					log.info("existsDocument--->>>> {}" , new Gson().toJson(data));
					return Mono.just(data);
				}).block();

	}

	public Mono<ResponseGeneralDocument> getComment(String hashProject, String typeDocument, String hash) {
		log.info("BlockChainClient.getComment.get==" + Constant.HOST_BLOCKCHAIN + Constant.API_URL_BLOCKCHAIN_COMMENT);
		return this.webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path(Constant.API_URL_BLOCKCHAIN_DOCUMENT)
						.queryParam("hashProject", hashProject)
						.queryParam("typeDocument", typeDocument)
						.queryParam("hash", hash)
						.build( ))
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

				.retrieve()
				.bodyToMono(ResponseGeneralDocument.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))

				.doOnError(e -> {
					log.error("doOnError: getComment " , e);
					throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

				}).map(data -> {
					log.info("getComment--->>>>{}" ,new Gson().toJson(data));
					return Mono.just(data);
				}).block();

	}
	
	public void addUserRoleProject(UserRoleProjectRequest request) {

		log.info("BlockChainClient.addUserRoleProject.post==" + Constant.HOST_BLOCKCHAIN + Constant.API_URL_BLOCKCHAIN_PROJECT_ROLE);
		
		this.webClient.post().uri(Constant.API_URL_BLOCKCHAIN_PROJECT_ROLE).accept(MediaType.APPLICATION_JSON)
		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
		.body(Mono.just(request), UserRoleProjectRequest.class)
		.retrieve()
		.bodyToMono(ResponseGeneral.class)
		.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
		.doOnError(e -> {
			insertRoleProject(request);
			log.error("doOnError: addUserRoleProject " ,e);
			throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}).subscribe(data -> {
			log.info("addUserRoleProject--->>>>{}" ,new Gson().toJson(data));
		});

	}
	

	
	private void insertRoleProject(UserRoleProjectRequest request) {


		UserRoleProjectQueue userQueu = new UserRoleProjectQueue();
		userQueu.setUser(request.getUser());
		userQueu.setProjectCodeHash(request.getProjectCodeHash());
		userQueu.setRole(request.getRole());
		userQueu.setProxyAddressUserSession(request.getProxyAddressUserSession());

		QueueTransaction tx = new QueueTransaction();
		tx.setUserRoleProject(userQueu);

		queueDao.addTransaction(tx);
	}
}
