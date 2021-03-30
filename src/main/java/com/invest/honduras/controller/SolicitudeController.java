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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.solicitude.ApproveSolicitudeRequest;
import com.invest.honduras.http.client.solicitude.SolicitudeUpdateRequest;
import com.invest.honduras.http.response.solicitude.SolicitudeResponse;
import com.invest.honduras.service.SolicitudeService;
import com.invest.honduras.util.Util;

import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("/api/v1/solicitude")
//@CrossOrigin(origins = "*")
public class SolicitudeController implements ApiSolicitudeController {

	@Autowired
	SolicitudeService solicitudeService;

	@GetMapping(value = "/find/{id}", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@Override
	public Mono<ResponseEntity<SolicitudeResponse>> findByIdSolicitude(@PathVariable(value = "id") String id) {
		return solicitudeService.findById(id)
				.map(data -> ResponseEntity
						.ok(new SolicitudeResponse(HttpStatus.OK.value(), data)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping(value = "/", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@Override
	public Mono<ResponseEntity<SolicitudeResponse>> addSolicitude(@RequestHeader("Authorization") String token,
			@RequestHeader("Role") String role) {

		UserSession userSession = new UserSession();
		userSession.setEmail(Util.decodeToken(token));

		return solicitudeService.createSolicitude(userSession).map(data -> ResponseEntity
				.ok(new SolicitudeResponse(HttpStatus.OK.value(), data)));
	}

	@PutMapping("/{id}")
	@Override
	public Mono<ResponseEntity<SolicitudeResponse>> updateSolicitude(@PathVariable(value = "id") String id,
			@RequestBody SolicitudeUpdateRequest updateRequest, @RequestHeader("Authorization") String token,
			@RequestHeader("Role") String role) {

		UserSession userSession = new UserSession();
		userSession.setEmail(Util.decodeToken(token));
		userSession.setRole(role);

		return solicitudeService.updateSolicitude(id, updateRequest, userSession).map(data -> ResponseEntity
				.ok(new SolicitudeResponse(HttpStatus.OK.value(), data)));

	}

	@PutMapping("/accredit/{idProject}")
	@Override
	public Mono<ResponseEntity<SolicitudeResponse>> accreditSolicitude(
			@PathVariable(value = "idProject") String idProject, @RequestBody ApproveSolicitudeRequest request,
			@RequestHeader("Authorization") String token, @RequestHeader("Role") String role) {

		UserSession userSession = new UserSession();
		userSession.setEmail(Util.decodeToken(token));
		userSession.setRole(role);

		if (request.isAccredited())
			return solicitudeService.accreditSolicitude(idProject, request, userSession).map(data -> ResponseEntity
					.ok(new SolicitudeResponse(HttpStatus.OK.value(), data)));
		else
			return solicitudeService.notAccredited(request, userSession).map(data -> ResponseEntity
					.ok(new SolicitudeResponse(HttpStatus.OK.value(), data)));
	}

}
