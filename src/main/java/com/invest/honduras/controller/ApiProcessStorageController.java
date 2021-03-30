package com.invest.honduras.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.invest.honduras.http.response.process.ProcessResponse;
import com.invest.honduras.http.response.solicitude.SolicitudeResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApiProcessStorageController {

 
	@ApiOperation(value = "Descarga un documento por el identificador del mismo",
			//produces = MediaType.APPLICATION_OCTET_STREAM_VALUE,
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,			
            httpMethod = "GET")	
	 @ApiResponses(value = {    		
     		 @ApiResponse(code = 200, message = "Éxito en descargar un documento "),
             @ApiResponse(code = 401, message = "No autorizado para descargar el documento"),
             @ApiResponse(code = 403, message = "El acceso para descarga un documento esta prohibido"),
             @ApiResponse(code = 404, message = "El recurso no pudo ser encontrado")
    })
	Mono<ResponseEntity<Resource>> downloadFile(@PathVariable(value = "id") String id);
	
	@ApiOperation(value = "Permite ingresar un documento al sistema",
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			response = SolicitudeResponse.class,
            httpMethod = "POST")
	 @ApiResponses(value = {    		
     		 @ApiResponse(code = 200, message = "Éxito en ingresar un documento "),
             @ApiResponse(code = 401, message = "No autorizado para ingresar el documento"),
             @ApiResponse(code = 403, message = "El acceso para ingresar un documento esta prohibido"),
             @ApiResponse(code = 404, message = "El recurso no pudo ser encontrado")
    })
    Mono<ResponseEntity<ProcessResponse>> uploadFile(@RequestBody Flux<Part> parts,
			@PathVariable(value = "id") String id, @RequestHeader("Authorization") String token,
			@RequestHeader("Role") String role);
  

    
}
