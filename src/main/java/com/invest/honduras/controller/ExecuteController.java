package com.invest.honduras.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.http.client.ExecuteCreateRequest;
import com.invest.honduras.http.client.execute.ExecuteRequest;
import com.invest.honduras.http.client.process.ApproveRequest;
import com.invest.honduras.http.response.ElementExists;
import com.invest.honduras.http.response.UserResponse;
import com.invest.honduras.http.response.execution.ExecuteResponse;
import com.invest.honduras.service.ExecuteService;
import com.invest.honduras.util.Util;

import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("/api/v1/execute")
//@CrossOrigin(origins = "*")
public class ExecuteController implements ApiExecuteController {

	@Autowired
	ExecuteService service;
	
	

	@GetMapping(value = "/exists-document", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@Override
	public Mono<ResponseEntity<ElementExists>> existsDocument(@RequestParam String idProject,
			@RequestParam String idExecute, @RequestParam String typeDocument, @RequestParam String hash) {
		return service.existsDocument(idProject, idExecute, typeDocument, hash)
				.flatMap(data -> Mono.just(ResponseEntity
						.ok(new ElementExists(HttpStatus.OK.value(), data))))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@GetMapping(value = "/exists-comment", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@Override
	public Mono<ResponseEntity<ElementExists>> existsComment(@RequestParam String idProject,
			@RequestParam String idExecute, @RequestParam String typeDocument, @RequestParam String hash) {
		return service.existsComment(idProject, idExecute, typeDocument, hash)
				.flatMap(data -> Mono.just(ResponseEntity
						.ok(new ElementExists(HttpStatus.OK.value(), data))))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PostMapping(value = "/{id-project}", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@Override
	public Mono<ResponseEntity<ExecuteResponse>> createExecute(@PathVariable(value = "id-project") String idProject, 
			@RequestBody ExecuteCreateRequest request,
			@RequestHeader("Authorization") String token,
			@RequestHeader("Role") String role) {

		UserSession userSession = new UserSession();
		userSession.setRole(role);
		userSession.setEmail(Util.decodeToken(token));

		return service.createExecute(idProject, request,userSession).map(data -> ResponseEntity
				.ok(new ExecuteResponse(HttpStatus.OK.value(), data)));
	}
	
	
	
	
	@PutMapping("/{id}")
	@Override
	public Mono<ResponseEntity<ExecuteResponse>> updateExecution(@PathVariable(value = "id") String id,
			@RequestBody ExecuteRequest request, @RequestHeader("Authorization") String token,
			@RequestHeader("Role") String role) {

		UserSession userSession = new UserSession();
		userSession.setEmail(Util.decodeToken(token));
		userSession.setRole(role);

		return service.updateExecute(id, request, userSession).map(data -> ResponseEntity
				.ok(new ExecuteResponse(HttpStatus.OK.value(), data)));
	}
	

	@GetMapping(value = "/find/{id}", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@Override
	public Mono<ResponseEntity<ExecuteResponse>> findByIdExecution(@PathVariable(value = "id") String id) {
		return service.findById(id)
				.map(data -> ResponseEntity
						.ok(new ExecuteResponse(HttpStatus.OK.value(), data)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@GetMapping(value = "/role/{codeRole}", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@Override
	public   Mono<ResponseEntity<UserResponse>> getUsersByRole(@PathVariable(value = "codeRole") String role){
		return service.getUsersByRole(role)
				.map(data -> ResponseEntity
						.ok(new UserResponse(HttpStatus.OK.value(), data)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PostMapping(value = "/review/{idExecute}", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@Override
	public Mono<ResponseEntity<ExecuteResponse>> approveDocument(@PathVariable(value = "idExecute") String idExecute,
			@RequestHeader("Authorization") String token, @RequestHeader("role") String role,
			@RequestBody ApproveRequest request) {
		UserSession userSession = new UserSession();
		userSession.setEmail(Util.decodeToken(token));
		userSession.setRole(role);
		return service.approveDocument(idExecute, request, userSession, false)
				.map(data -> ResponseEntity
						.ok(new ExecuteResponse(HttpStatus.OK.value(), data)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PostMapping(value = "/accredited/{idExecute}", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@Override
	public Mono<ResponseEntity<ExecuteResponse>> acreditedDocument(@PathVariable(value = "idExecute") String idExecute,
			@RequestHeader("Authorization") String token, @RequestHeader("role") String role,
			@RequestBody ApproveRequest request) {
		UserSession userSession = new UserSession();
		userSession.setEmail(Util.decodeToken(token));
		userSession.setRole(role);
		return service.approveDocument(idExecute, request, userSession, true)
				.map(data -> ResponseEntity
						.ok(new ExecuteResponse(HttpStatus.OK.value(), data)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}


}
