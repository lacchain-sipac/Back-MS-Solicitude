package com.invest.honduras.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.invest.honduras.http.client.ProcessInitRequest;
import com.invest.honduras.http.client.process.ApproveRequest;
import com.invest.honduras.http.client.process.CommitteeDeleteRequest;
import com.invest.honduras.http.client.process.CommitteeRequest;
import com.invest.honduras.http.client.process.CommitteeUpdateRequest;
import com.invest.honduras.http.client.process.ProcessRequest;
import com.invest.honduras.http.response.ElementExists;
import com.invest.honduras.http.response.process.ProcessResponse;

import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

public interface ApiProcessController {

	 @ApiOperation(
	            value = " init Process",
	            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
	            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ProcessResponse.class,
	            httpMethod = "POST" 
	            )
	Mono<ResponseEntity<ProcessResponse>> initProcess(@PathVariable(value = "id-process") String idProcess,@RequestBody ProcessInitRequest request, @RequestHeader("Authorization") String token,
			@RequestHeader("Role") String role);
    @ApiOperation(
            value = " update Process",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ProcessResponse.class,
            httpMethod = "PUT" 
            )    
    Mono<ResponseEntity<ProcessResponse>> updateProcess(@PathVariable(value = "id") String id,
    		@RequestBody ProcessRequest request,
    		@RequestHeader("Authorization") String token,
    		@RequestHeader("role") String role);
 
    @ApiOperation(
            value = "get Process by id solicitude",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ProcessResponse.class,
            httpMethod = "GET" 
            )    
     Mono<ResponseEntity<ProcessResponse>> findByIdProcess(@PathVariable(value="id") String id);

    
    @ApiOperation(
            value = "add user Committee",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ProcessResponse.class,
            httpMethod = "POST" 
            )
    Mono<ResponseEntity<ProcessResponse>> addUserCommittee(@PathVariable(value = "idProcess") String idProcess, @RequestHeader("Authorization") String token,
			@RequestHeader("role") String role, @RequestBody CommitteeRequest request);

    @ApiOperation(
            value = " update user Committee",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ProcessResponse.class,
            httpMethod = "PUT" 
            )    
    Mono<ResponseEntity<ProcessResponse>> updateUserCommittee(@PathVariable(value = "idProcess") String idProcess,
    		@RequestBody CommitteeUpdateRequest request, 
    		@RequestHeader("Authorization") String token, 
    		@RequestHeader("role") String role);

    
    @ApiOperation(
            value = " delete user Committee",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ProcessResponse.class,
            httpMethod = "DELETE" 
            )  
    Mono<ResponseEntity<ProcessResponse>> deleteUserCommittee(
			@RequestBody  CommitteeDeleteRequest request,
			@RequestHeader("Authorization") String token,
			@RequestHeader("Role") String role);
    
    
    
    @ApiOperation(
            value = "aprovee  document",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ProcessResponse.class,
            httpMethod = "POST" 
            )
    Mono<ResponseEntity<ProcessResponse>> approveDocument(@PathVariable(value = "idProcess") String idProcess, @RequestHeader("Authorization") String token,
			@RequestHeader("role") String role, @RequestBody ApproveRequest request);


    @ApiOperation(
            value = "acredited  document",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ProcessResponse.class,
            httpMethod = "POST" 
            )
    Mono<ResponseEntity<ProcessResponse>> acreditedDocument(@PathVariable(value = "idProcess") String idProcess, 
    		@RequestHeader("Authorization") String token,
			@RequestHeader("role") String role, @RequestBody ApproveRequest request);
    

    @ApiOperation(
            value = "exists document",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ElementExists.class,
            httpMethod = "GET" 
            )
    Mono<ResponseEntity<ElementExists>> existsDocument(@RequestParam String hashProject,
			@RequestParam String idProcess,
			@RequestParam String typeDocument, @RequestParam String hash);

    @ApiOperation(
            value = "exists comment",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, response = ElementExists.class,
            httpMethod = "GET" 
            )
    Mono<ResponseEntity<ElementExists>> existsComment(@RequestParam String hashProject,
			@RequestParam String idProcess,
			@RequestParam String typeDocument, @RequestParam String hash);

}
