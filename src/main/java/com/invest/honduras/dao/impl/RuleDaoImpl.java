package com.invest.honduras.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.invest.honduras.dao.RuleDao;
import com.invest.honduras.domain.model.project.Rule;
import com.invest.honduras.repository.RuleRepository;

import reactor.core.publisher.Mono;

@Component
public class RuleDaoImpl implements RuleDao {

	@Autowired
	RuleRepository proyectRepository;


	@Override
	public Mono<Rule> findByCode(String code) {
		return proyectRepository.findByCode(code);
	}
	
	

}
