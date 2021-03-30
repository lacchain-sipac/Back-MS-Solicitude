package com.invest.honduras.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.invest.honduras.dao.ConfigurationDao;
import com.invest.honduras.dao.RuleDao;
import com.invest.honduras.domain.model.Configuration;
import com.invest.honduras.domain.model.project.Rule;
import com.invest.honduras.util.Constants;

import lombok.Getter;

@Component
@Getter
public class ConfigurationProject {

	private Rule rule;

	private Configuration configuration;
	
	@Autowired
	RuleDao ruleDao;

	@Autowired
	ConfigurationDao configurationDao;
	
	@Bean
	public void getRuleProject() {
		ruleDao.findByCode(Constants.CODE_PROJECT).subscribe(item -> {
			rule = item;
		});
		
		
		configuration =configurationDao.find();
	}
}
