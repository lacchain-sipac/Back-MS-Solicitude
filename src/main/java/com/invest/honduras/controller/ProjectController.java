package com.invest.honduras.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.http.client.ProjectFinishRequest;
import com.invest.honduras.http.response.ElementExists;
import com.invest.honduras.http.response.ProjectAllResponse;
import com.invest.honduras.http.response.ProjectResponse;
import com.invest.honduras.service.ProjectService;
import com.invest.honduras.util.Util;

import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("/api/v1/project-invest")
//@CrossOrigin(origins = "*")
public class ProjectController implements ApiProjectController {

	@Autowired
	ProjectService service;

	@GetMapping(value = "/exists/{id}", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@Override
	public Mono<ResponseEntity<ElementExists>> existsProject(@PathVariable(value = "id") String id ) {
		return service.existsProject(id).flatMap(data ->  Mono.just(ResponseEntity
					.ok(new ElementExists(HttpStatus.OK.value(), data))))
		.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@GetMapping(value = "/find-all", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@Override
	public Mono<ResponseEntity<ProjectAllResponse>> findAllProjects(@RequestHeader("Role") String role) {
		return service.findAllProject().flatMap(data ->  Mono.just(ResponseEntity
					.ok(new ProjectAllResponse(HttpStatus.OK.value(), data))))
		.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@GetMapping(value = "/find/{id}", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@Override
	public Mono<ResponseEntity<ProjectResponse>> findById(@PathVariable(value = "id") String id, @RequestHeader("Role") String role) {
		return service.findById(id)
				.map(data -> ResponseEntity
						.ok(new ProjectResponse(HttpStatus.OK.value(), data)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PutMapping("/financial-closure/{id}")
	@Override
	public  Mono<ResponseEntity<ProjectResponse>> financiaClosure(@PathVariable(value = "id") String id, 
			@RequestBody ProjectFinishRequest request,
    		@RequestHeader("Authorization") String token,
    		@RequestHeader("role") String role){
		
		UserSession userSession = new UserSession();
		userSession.setEmail(Util.decodeToken(token));
		userSession.setRole(role);
		
		return service.financiaClosure(id, request, userSession)
				.map(data -> ResponseEntity
						.ok(new ProjectResponse(HttpStatus.OK.value(), data)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

}
