package com.invest.honduras.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.invest.honduras.domain.model.solicitude.ApproveSolicitudeRequest;
import com.invest.honduras.http.client.solicitude.SolicitudeUpdateRequest;
import com.invest.honduras.http.response.solicitude.SolicitudeResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Mono;

public interface ApiSolicitudeController {

    @ApiOperation(
            value = "Adiciona una nueva solicitud",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = SolicitudeResponse.class,
            httpMethod = "POST" 
            ) 
    @ApiResponses(value = {    		
             		 @ApiResponse(code = 200, message = "Éxito en registrar el inicio del documento de solicitud"),
                     @ApiResponse(code = 401, message = "No autorizado para registrar el documento"),
                     @ApiResponse(code = 403, message = "El acceso al registro esta prohibido"),
                     @ApiResponse(code = 404, message = "El recurso no pudo ser encontrado")
            })
    Mono<ResponseEntity<SolicitudeResponse>> addSolicitude(@RequestHeader("Authorization") String token, @RequestHeader("role") String role);

    @ApiOperation(
            value = "Actualiza una solicitud existente por el identificador de la solicitud",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = SolicitudeResponse.class,
            httpMethod = "PUT" 
            )
    @ApiResponses(value = {    		
    		 @ApiResponse(code = 200, message = "Éxito en actualizar una solicitud"),
            @ApiResponse(code = 401, message = "No autorizado para actualizar la solicitud"),
            @ApiResponse(code = 403, message = "El acceso para actualizar esta prohibido"),
            @ApiResponse(code = 404, message = "La solicitud no pudo ser encontrado para ser actualziado")
   })
    Mono<ResponseEntity<SolicitudeResponse>> updateSolicitude(@PathVariable(value = "id") String id,
    		@RequestBody SolicitudeUpdateRequest updateRequest, 
    		@RequestHeader("Authorization") String token, 
    		@RequestHeader("role") String role);
 
    @ApiOperation(
            value = " busca una solicitud a través del identificador de la misma",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, response = SolicitudeResponse.class,
            httpMethod = "GET" 
            )
    @ApiResponses(value = {    		
   		 @ApiResponse(code = 200, message = "Éxito en la búsqueda una solicitud"),
           @ApiResponse(code = 401, message = "No autorizado para la solicitud"),
           @ApiResponse(code = 403, message = "El acceso para la busqueda esta prohibido"),
           @ApiResponse(code = 404, message = "La solicitud no pudo ser encontrado")
  })
     Mono<ResponseEntity<SolicitudeResponse>> findByIdSolicitude(@PathVariable(value="id") String id);

 
    @ApiOperation(
            value = " Accredita una solicitud contra la blockchain",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, response = SolicitudeResponse.class,
            httpMethod = "PUT" 
            )   
    @ApiResponses(value = {    		
      		 @ApiResponse(code = 200, message = "Éxito en la acreditación de una solicitud"),
              @ApiResponse(code = 401, message = "No autorizado para la acreditación"),
              @ApiResponse(code = 403, message = "El acceso para la acreditación esta prohibido"),
              @ApiResponse(code = 404, message = "La solicitud no pudo ser encontrado")
     })
    Mono<ResponseEntity<SolicitudeResponse>> accreditSolicitude(@PathVariable(value = "idProject") String idProject, 
    		@RequestBody ApproveSolicitudeRequest request,
    		@RequestHeader("Authorization") String token, 
    		@RequestHeader("Role") String role);

}
