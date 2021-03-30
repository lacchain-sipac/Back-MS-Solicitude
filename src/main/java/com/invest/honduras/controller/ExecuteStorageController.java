package com.invest.honduras.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.http.response.execution.ExecuteResponse;
import com.invest.honduras.service.ExecuteStorageService;
import com.invest.honduras.util.Util;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("/api/v1/execute/document")
//@CrossOrigin(origins = "*")
public class ExecuteStorageController implements ApiExecuteStorageController {

	@Autowired
	ExecuteStorageService soService;

	@RequestMapping(
			value = "/{idExecute}", 
			method = RequestMethod.POST, 
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	
	public Mono<ResponseEntity<ExecuteResponse>> uploadFile(
			@RequestBody Flux<Part> parts,
			@PathVariable(value = "idExecute") String idExecute, 
			@RequestHeader("Authorization") String token,
			@RequestHeader("Role") String role) {


		UserSession userSession = new UserSession();
		userSession.setEmail(Util.decodeToken(token));
		userSession.setRole(role);

		return parts.collectList().flatMap(list -> soService.create(idExecute, list, userSession))
				.map(data -> ResponseEntity.ok(new ExecuteResponse(HttpStatus.OK.value(), data)));

	}

	@GetMapping(
			value = "/{id}", 
			produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public Mono<ResponseEntity<Resource>> downloadFile(@PathVariable(value = "id") String id) {

		return soService.findById(id).map(storage -> {

			HttpHeaders headers = new HttpHeaders();

			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + storage.getFileName() + "\"");

			if (!StringUtils.isEmpty(storage.getContentType())) {
				headers.setContentType(MediaType.parseMediaType(storage.getContentType()));
			}

			return ResponseEntity.ok().cacheControl(CacheControl.noCache()).headers(headers)
					.body(storage.getFile());
		});
	}



}
