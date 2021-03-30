package com.invest.honduras.component.impl;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Component
@Data
@Getter
@Setter

public class ServerConfig {

	ServerHttpRequest request;
	ServerHttpResponse response;
}
