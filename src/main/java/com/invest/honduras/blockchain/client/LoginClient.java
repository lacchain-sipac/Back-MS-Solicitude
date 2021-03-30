package com.invest.honduras.blockchain.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.invest.honduras.error.GlobalException;
import com.invest.honduras.http.client.LoginResponse;
import com.invest.honduras.util.Constant;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoginClient {

	private final WebClient webClient;

	public LoginClient() {
		this.webClient = WebClient.builder().baseUrl(Constant.HOST_GATEWAY)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

	public Mono<LoginResponse> refreshToken(String token) {
				
        Mono<LoginResponse> clientResponse ;	        

        clientResponse = this.webClient.post().uri(Constant.API_URL_REFRESH_TOKEN)
				.accept(MediaType.APPLICATION_JSON).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.header ( HttpHeaders.AUTHORIZATION, token)
				.exchange()
				.doOnError(e -> {
					log.info("LoginClient.doOnError: " + e.getMessage());
					throw new RuntimeException();
				}).flatMap(response -> {
	                if (response.statusCode().equals(HttpStatus.UNAUTHORIZED))
	                	throw new GlobalException(HttpStatus.UNAUTHORIZED, Constant.MESSAGE_REFRESH_TOKEN_UNAUTHORIZED);
	                else if (response.statusCode().is4xxClientError())
	                	throw new GlobalException(HttpStatus.BAD_REQUEST, Constant.MESSAGE_REFRESH_TOKEN_BAD_REQUEST);	                	
	                else if (response.statusCode().is5xxServerError())
	                	throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, Constant.MESSAGE_REFRESH_TOKEN_ERROR);
	                else if (!response.statusCode().is2xxSuccessful())
	                	throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, Constant.MESSAGE_REFRESH_TOKEN_ERROR);	                
					return  response.bodyToMono(LoginResponse.class);
				});
        //.subscribe(data -> {
		//			System.out.println("enviando correo--->>>>" + data.statusCode());
		//		});
		return clientResponse;
	}

}
