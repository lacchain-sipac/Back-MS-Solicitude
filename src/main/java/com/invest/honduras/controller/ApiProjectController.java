package com.invest.honduras.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.invest.honduras.http.client.ProjectFinishRequest;
import com.invest.honduras.http.response.ElementExists;
import com.invest.honduras.http.response.ProjectAllResponse;
import com.invest.honduras.http.response.ProjectResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Mono;

public interface ApiProjectController {
	 @ApiOperation(
	            value = "Permite verificar si elproyecto esta registrado en la blocchain",
	            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
	            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ElementExists.class,
	            httpMethod = "GET" 
	            )
	   @ApiResponses(value = {    		
       		 @ApiResponse(code = 200, message = "Retorna si el proyecto existe en la blockchain"),
               @ApiResponse(code = 401, message = "No autorizado para hacer la consulta"),
               @ApiResponse(code = 403, message = "El acceso a la consulta esta prohibido"),
               @ApiResponse(code = 404, message = "El recurso no pudo ser encontrado")
      })
	Mono<ResponseEntity<ElementExists>> existsProject(@RequestHeader("id") String id);
	
    @ApiOperation(
            value = " Permite realizar búsquedas de proyectos",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ProjectAllResponse.class,
            httpMethod = "GET" 
            )
    @ApiResponses(value = {    		
      		 @ApiResponse(code = 200, message = "Retorna la lista de proyectos"),
              @ApiResponse(code = 401, message = "No autorizado para hacer la consulta"),
              @ApiResponse(code = 403, message = "El acceso a la consulta esta prohibido"),
              @ApiResponse(code = 404, message = "El recurso no pudo ser encontrado")
     })
    Mono<ResponseEntity<ProjectAllResponse>> findAllProjects(@RequestHeader("Role") String role);
    
    
    @ApiOperation(
            value = "Permite realizar la búsqueda por su identificador",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ProjectResponse.class,
            httpMethod = "GET" 
            )
    @ApiResponses(value = {    		
     		 @ApiResponse(code = 200, message = "Retorna el resultado de la búsqueda"),
             @ApiResponse(code = 401, message = "No autorizado para hacer la consulta"),
             @ApiResponse(code = 403, message = "El acceso a la consulta esta prohibido"),
             @ApiResponse(code = 404, message = "El recurso no pudo ser encontrado")
    })
    Mono<ResponseEntity<ProjectResponse>> findById(@PathVariable(value="id") String id, @RequestHeader("Role") String role);
    
    @ApiOperation(
            value = " Permite realizar el cierre del proyecto",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ProjectResponse.class,
            httpMethod = "PUT" 
            )
    @ApiResponses(value = {    		
    		 @ApiResponse(code = 200, message = "Cierre del proyecto Éxitoso"),
            @ApiResponse(code = 401, message = "No autorizado para realizar el cierre del proyecto"),
            @ApiResponse(code = 403, message = "El acceso a la función esta prohibido"),
            @ApiResponse(code = 404, message = "El recurso no pudo ser encontrado")
   })
    Mono<ResponseEntity<ProjectResponse>> financiaClosure(@PathVariable(value = "id") String id, 
    		@RequestBody ProjectFinishRequest request,
    		@RequestHeader("Authorization") String token,
    		@RequestHeader("role") String role);
 
    
}
