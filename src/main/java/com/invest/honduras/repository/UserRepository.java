package com.invest.honduras.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.invest.honduras.domain.model.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

	// @Query(value = "{'name': ?0,'lastName':?1 }")
	Flux<User> findByFullnameLikeIgnoreCaseOrEmailLikeIgnoreCase(String fullname, String email, Sort sort);

	@Query(value = "{'roles.code': ?0 }")
	Flux<User> findUserByRol(final String code);

	Mono<User> findByEmail(final String email);

	@Query(value ="{ 'roles.code' : { $in : ?0 } }")
	Flux<User> findByRolesCodeIn(final List<String> roles);
	
	@Query(value ="{ 'id' : { $in : ?0 } }")
	Flux<User> findByIdsIn(final List<String> ids);
	
	
}
