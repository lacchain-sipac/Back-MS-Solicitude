package com.invest.honduras.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.invest.honduras.dao.ConfigurationDao;
import com.invest.honduras.domain.model.Configuration;
import com.invest.honduras.repository.ConfigurationRepository;

@Component
public class ConfigurationDaoImpl implements ConfigurationDao {

	@Autowired
	ConfigurationRepository repo;

	@Override
	public Configuration find() {
		return repo.findAll().get(0);

	}

}
