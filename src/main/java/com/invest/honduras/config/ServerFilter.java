package com.invest.honduras.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.invest.honduras.blockchain.client.LoginClient;
import com.invest.honduras.component.impl.ServerConfig;
import com.invest.honduras.dao.UserDao;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.http.client.RoleRequest;
import com.invest.honduras.util.Constant;
import com.invest.honduras.util.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ServerFilter implements WebFilter {

	@Autowired
	ServerConfig serverConfig;

	@Autowired
	UserDao userDao;

	@Autowired
	LoginClient loginClient;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();

		serverConfig.setRequest(request);
		serverConfig.setResponse(response);

		// request.getPath().value();

		if (request.getHeaders().containsKey("Authorization")) {

			String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

			if (request.getHeaders().containsKey("Role") || request.getHeaders().containsKey("role")) {

				final String role = !StringUtils.isEmpty(request.getHeaders().getFirst("Role"))
						? request.getHeaders().getFirst("Role")
						: request.getHeaders().getFirst("role");

				log.info("ServerFilter.role:{}, email:{}", role, Util.decodeToken(authHeader));

				return userDao.findByEmail(Util.decodeToken(authHeader)).flatMap(item -> {
					
					boolean idExists = item.getRoles().stream().map(RoleRequest::getCode).anyMatch(role::equals);
					
					log.info("ServerFilter.role:{}, email:{}, idExists:{}", role, Util.decodeToken(authHeader), idExists);


					if (!idExists)
						throw new GlobalException(HttpStatus.BAD_REQUEST, Constant.MESSAGE_ROLE_NOT_FOUND);

					return loginClient.refreshToken(authHeader).flatMap(resp -> {
						exchange.getResponse().getHeaders().add("new-token", resp.getDataResponse().getToken());
						return chain.filter(exchange);

					});
				});

			} else {
				// String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
				return loginClient.refreshToken(authHeader).flatMap(resp -> {
					exchange.getResponse().getHeaders().add("new-token", resp.getDataResponse().getToken());
					return chain.filter(exchange);

				});
			}

		} else {
			return chain.filter(exchange);
		}

	}

}
