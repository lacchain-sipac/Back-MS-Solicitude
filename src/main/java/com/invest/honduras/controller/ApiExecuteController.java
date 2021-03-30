package com.invest.honduras.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.invest.honduras.http.client.ExecuteCreateRequest;
import com.invest.honduras.http.client.execute.ExecuteRequest;
import com.invest.honduras.http.client.process.ApproveRequest;
import com.invest.honduras.http.response.ElementExists;
import com.invest.honduras.http.response.UserResponse;
import com.invest.honduras.http.response.execution.ExecuteResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Mono;

public interface ApiExecuteController {

	   @ApiOperation(
	            value = "Permite iniciar la ejecución de un proyecto",
	            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
	            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ExecuteResponse.class,
	            httpMethod = "POST" 
	            )
	    @ApiResponses(value = {    		
        		 @ApiResponse(code = 200, message = "Éxito en iniciar la ejecución de un proyecto"),
                @ApiResponse(code = 401, message = "No autorizado para iniciar la ejecución de un proyecto"),
                @ApiResponse(code = 403, message = "El acceso a la tarea esta prohibido"),
                @ApiResponse(code = 404, message = "El recurso no pudo ser encontrado")
       })
	Mono<ResponseEntity<ExecuteResponse>> createExecute(@PathVariable(value = "idProject") String idProject,
			@RequestBody ExecuteCreateRequest request,
			@RequestHeader("Authorization") String token,
			@RequestHeader("Role") String role);
	
    @ApiOperation(
            value = "Permite actualizar los datos de la ejecución del proyecto",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ExecuteResponse.class,
            httpMethod = "PUT" 
            )
    @ApiResponses(value = {    		
   		 @ApiResponse(code = 200, message = "Éxito en actualizar los datos de la ejecución del proyecto"),
           @ApiResponse(code = 401, message = "No autorizado para actualizar la ejecución de un proyecto"),
           @ApiResponse(code = 403, message = "El acceso a la tarea esta prohibido"),
           @ApiResponse(code = 404, message = "El recurso no pudo ser encontrado")
  })
    Mono<ResponseEntity<ExecuteResponse>> updateExecution(@PathVariable(value = "id") String id,
    		@RequestBody ExecuteRequest request,
    		@RequestHeader("Authorization") String token,
    		@RequestHeader("role") String role);
 
    @ApiOperation(
            value = "Permite obtener la entidad de ejecución por su identificador",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ExecuteResponse.class,
            httpMethod = "GET" 
            )   
    @ApiResponses(value = {    		
      		 @ApiResponse(code = 200, message = "Éxito en obtener los datos de la ejecución del proyecto"),
              @ApiResponse(code = 401, message = "No autorizado para obtener los datos de la ejecución"),
              @ApiResponse(code = 403, message = "El acceso al recurso esta prohibido"),
              @ApiResponse(code = 404, message = "El recurso no pudo ser encontrado")
     })
     Mono<ResponseEntity<ExecuteResponse>> findByIdExecution(@PathVariable(value="id") String id);

    
    @ApiOperation(
            value = "Permite obtener los usuarios por un rol específico ",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = UserResponse.class,
            httpMethod = "GET" 
            )
    @ApiResponses(value = {    		
     		 @ApiResponse(code = 200, message = "Éxito en obtener los usuarios por un rol específico"),
             @ApiResponse(code = 401, message = "No autorizado para obtener la ejecución de un proyecto"),
             @ApiResponse(code = 403, message = "El acceso al recurso esta prohibido"),
             @ApiResponse(code = 404, message = "El recurso no pudo ser encontrado")
    })
    Mono<ResponseEntity<UserResponse>> getUsersByRole(@PathVariable(value = "role") String role);

    @ApiOperation(
            value = "Permite realizar la tarea de aprovar un documento en la blockchain",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ExecuteResponse.class,
            httpMethod = "POST" 
            )
    @ApiResponses(value = {    		
    		 @ApiResponse(code = 200, message = "Éxito en aprovar un documento en la blockchain"),
            @ApiResponse(code = 401, message = "No autorizado para aprovar un documento en la blockchain"),
            @ApiResponse(code = 403, message = "El acceso al recurso esta prohibido"),
            @ApiResponse(code = 404, message = "El recurso no pudo ser encontrado")
   })
    Mono<ResponseEntity<ExecuteResponse>> approveDocument(@PathVariable(value = "idExecute") String idExecute,
			@RequestHeader("Authorization") String token, @RequestHeader("role") String role,
			@RequestBody ApproveRequest request);

    @ApiOperation(
            value = "Permite la tarea de acreditar un documento en la blockchain",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ExecuteResponse.class,
            httpMethod = "POST" 
            )
    @ApiResponses(value = {    		
   		 @ApiResponse(code = 200, message = "Éxito en acreditar un documento en la blockchain"),
           @ApiResponse(code = 401, message = "No autorizado en acreditar un documento en la blockchain"),
           @ApiResponse(code = 403, message = "El acceso al recurso esta prohibido"),
           @ApiResponse(code = 404, message = "El recurso no pudo ser encontrado")
    })
    Mono<ResponseEntity<ExecuteResponse>> acreditedDocument(@PathVariable(value = "idExecute") String idExecute,
			@RequestHeader("Authorization") String token, @RequestHeader("role") String role,
			@RequestBody ApproveRequest request) ;

    @ApiOperation(
            value = "Permite validar la existencia de un documento en la blockchain",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ElementExists.class,
            httpMethod = "GET" 
            )
    @ApiResponses(value = {    		
      		 @ApiResponse(code = 200, message = "Éxito en verificar un documento en la blockchain"),
              @ApiResponse(code = 401, message = "No autorizado en verificar un documento en la blockchain"),
              @ApiResponse(code = 403, message = "El acceso al recurso esta prohibido"),
              @ApiResponse(code = 404, message = "El recurso no pudo ser encontrado")
       })
    Mono<ResponseEntity<ElementExists>> existsDocument(@RequestParam String hashProject,
			@RequestParam String idProcess,
			@RequestParam String typeDocument, @RequestParam String hash);

    @ApiOperation(
            value = "Permite validar la existencia de un comentario en la blockchain ",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ElementExists.class,
            httpMethod = "GET" 
            )
    @ApiResponses(value = {    		
     		 @ApiResponse(code = 200, message = "Éxito en verificar un comentario en la blockchain"),
             @ApiResponse(code = 401, message = "No autorizado en verificar un comentario en la blockchain"),
             @ApiResponse(code = 403, message = "El acceso al recurso esta prohibido"),
             @ApiResponse(code = 404, message = "El recurso no pudo ser encontrado")
      })
    Mono<ResponseEntity<ElementExists>> existsComment(@RequestParam String hashProject,
			@RequestParam String idProcess,
			@RequestParam String typeDocument, @RequestParam String hash);

}
