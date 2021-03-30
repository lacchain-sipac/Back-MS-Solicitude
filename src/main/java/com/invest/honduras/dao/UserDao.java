package com.invest.honduras.dao;
import java.util.List;

import com.invest.honduras.domain.model.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
public interface UserDao {

	Mono<User> findByEmail(String email);

	Flux<User> findUserByRol(String rol);
	
	Flux<User> findUserByRoles(List<String> roles);
	
	Flux<User> findUserByIds(List<String> ids);
}
