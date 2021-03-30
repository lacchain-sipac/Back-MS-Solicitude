package com.invest.honduras.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.invest.honduras.http.client.ProcessInitRequest;
import com.invest.honduras.http.client.process.ApproveRequest;
import com.invest.honduras.http.client.process.CommitteeDeleteRequest;
import com.invest.honduras.http.client.process.CommitteeRequest;
import com.invest.honduras.http.client.process.CommitteeUpdateRequest;
import com.invest.honduras.http.client.process.ProcessRequest;
import com.invest.honduras.http.response.ElementExists;
import com.invest.honduras.http.response.process.ProcessResponse;
import com.invest.honduras.service.ProcessService;
import com.invest.honduras.util.Util;

import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("/api/v1/process")
//@CrossOrigin(origins = "*")

public class ProcessController implements ApiProcessController {

	@Autowired
	ProcessService service;

	@GetMapping(value = "/exists-document", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@Override
	public Mono<ResponseEntity<ElementExists>> existsDocument(@RequestParam String idProject,
			@RequestParam String idProcess, @RequestParam String typeDocument, @RequestParam String hash) {
		return service.existsDocument(idProject, idProcess, typeDocument, hash)
				.flatMap(data -> Mono.just(ResponseEntity
						.ok(new ElementExists(HttpStatus.OK.value(), data))))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@GetMapping(value = "/exists-comment", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@Override
	public Mono<ResponseEntity<ElementExists>> existsComment(@RequestParam String idProject,
			@RequestParam String idProcess, @RequestParam String typeDocument, @RequestParam String hash) {
		return service.existsComment(idProject, idProcess, typeDocument, hash)
				.flatMap(data -> Mono.just(ResponseEntity
						.ok(new ElementExists(HttpStatus.OK.value(), data))))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping(value = "/{id-project}", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@Override
	public Mono<ResponseEntity<ProcessResponse>> initProcess(@PathVariable(value = "id-project") String idProject,
			@RequestBody ProcessInitRequest request, @RequestHeader("Authorization") String token,
			@RequestHeader("Role") String role) {

		UserSession userSession = new UserSession();
		userSession.setRole(role);
		userSession.setEmail(Util.decodeToken(token));

		return service.initProcess(idProject, request, userSession).map(data -> ResponseEntity
				.ok(new ProcessResponse(HttpStatus.OK.value(), data)));
	}

	@PutMapping("/{id}")
	@Override
	public Mono<ResponseEntity<ProcessResponse>> updateProcess(@PathVariable(value = "id") String id,
			@RequestBody ProcessRequest request, @RequestHeader("Authorization") String token,
			@RequestHeader("Role") String role) {

		UserSession userSession = new UserSession();
		userSession.setEmail(Util.decodeToken(token));
		userSession.setRole(role);

		return service.updateProcess(id, request, userSession).map(data -> ResponseEntity
				.ok(new ProcessResponse(HttpStatus.OK.value(), data)));
	}

	@GetMapping(value = "/find/{id}", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@Override
	public Mono<ResponseEntity<ProcessResponse>> findByIdProcess(@PathVariable(value = "id") String id) {
		return service.findById(id)
				.map(data -> ResponseEntity
						.ok(new ProcessResponse(HttpStatus.OK.value(), data)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping(value = "/committee/{idProcess}", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@Override
	public Mono<ResponseEntity<ProcessResponse>> addUserCommittee(@PathVariable(value = "idProcess") String idProcess,
			@RequestHeader("Authorization") String token, @RequestHeader("role") String role,
			@RequestBody CommitteeRequest request) {

		UserSession userSession = new UserSession();
		userSession.setEmail(Util.decodeToken(token));
		userSession.setRole(role);

		return service.addUserCommittee(idProcess, request, userSession)
				.map(data -> ResponseEntity
						.ok(new ProcessResponse(HttpStatus.OK.value(), data)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PutMapping("/committee/{idProcess}")
	@Override
	public Mono<ResponseEntity<ProcessResponse>> updateUserCommittee(
			@PathVariable(value = "idProcess") String idProcess, @RequestBody CommitteeUpdateRequest request,
			@RequestHeader("Authorization") String token, @RequestHeader("role") String role) {

		UserSession userSession = new UserSession();
		userSession.setEmail(Util.decodeToken(token));
		userSession.setRole(role);

		return service.updateUserCommittee(idProcess, request, userSession)
				.map(data -> ResponseEntity
						.ok(new ProcessResponse(HttpStatus.OK.value(), data)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping(value = "/committee/", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@Override
	public Mono<ResponseEntity<ProcessResponse>> deleteUserCommittee(@RequestBody CommitteeDeleteRequest request,
			@RequestHeader("Authorization") String token, @RequestHeader("Role") String role) {

		UserSession userSession = new UserSession();
		userSession.setEmail(Util.decodeToken(token));
		userSession.setRole(role);

		return service.deleteUserCommittee(request, userSession)
				.map(data -> ResponseEntity
						.ok(new ProcessResponse(HttpStatus.OK.value(), data)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping(value = "/review/{idProcess}", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@Override
	public Mono<ResponseEntity<ProcessResponse>> approveDocument(@PathVariable(value = "idProcess") String idProcess,
			@RequestHeader("Authorization") String token, @RequestHeader("role") String role,
			@RequestBody ApproveRequest request) {
		UserSession userSession = new UserSession();
		userSession.setEmail(Util.decodeToken(token));
		userSession.setRole(role);
		return service.approveDocument(idProcess, request, userSession, false)
				.map(data -> ResponseEntity
						.ok(new ProcessResponse(HttpStatus.OK.value(), data)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping(value = "/accredited/{idProcess}", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@Override
	public Mono<ResponseEntity<ProcessResponse>> acreditedDocument(@PathVariable(value = "idProcess") String idProcess,
			@RequestHeader("Authorization") String token, @RequestHeader("role") String role,
			@RequestBody ApproveRequest request) {
		UserSession userSession = new UserSession();
		userSession.setEmail(Util.decodeToken(token));
		userSession.setRole(role);
		return service.approveDocument(idProcess, request, userSession, true)
				.map(data -> ResponseEntity
						.ok(new ProcessResponse(HttpStatus.OK.value(), data)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

}
