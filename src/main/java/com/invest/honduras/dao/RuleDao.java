package com.invest.honduras.dao;

import com.invest.honduras.domain.model.project.Rule;

import reactor.core.publisher.Mono;

public interface RuleDao {
	 Mono<Rule> findByCode(String code);
}
