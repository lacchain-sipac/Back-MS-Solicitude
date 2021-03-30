package com.invest.honduras.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.invest.honduras.dao.UserDao;
import com.invest.honduras.domain.model.User;
import com.invest.honduras.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class UserDaoImpl implements UserDao {

	@Autowired
	UserRepository userRepository;

	@Override
	public Mono<User> findByEmail(String email) {
		return userRepository.findByEmail(email);

	}

	@Override
	public Flux<User> findUserByRol(String rol) {
		return userRepository.findUserByRol(rol);
	}

	@Override
	public Flux<User> findUserByRoles(List<String> roles) {
		return userRepository.findByRolesCodeIn(roles);
	}
	
	@Override
	public Flux<User> findUserByIds(List<String> ids) {
		return userRepository.findByIdsIn(ids);
	}
}
