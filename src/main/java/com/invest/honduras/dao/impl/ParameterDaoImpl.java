package com.invest.honduras.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.invest.honduras.dao.ParameterDao;
import com.invest.honduras.domain.model.solicitude.Parameter;
import com.invest.honduras.domain.model.solicitude.StatusSolicitude;
import com.invest.honduras.repository.ParameterRepository;
import com.invest.honduras.repository.StatusSolicitudeRepository;

@Component
public class ParameterDaoImpl implements ParameterDao {
		
	@Autowired
	ParameterRepository parameterRepository;
	
	@Autowired
	StatusSolicitudeRepository statusSolicitudeRepository;
	
	@Override	
	public List<Parameter> listParameter() { 
		return parameterRepository.findAll();
	}

	@Override
	public List<StatusSolicitude> listStatusSolicitude() {
		return statusSolicitudeRepository.findAll();
	}
	
	
}
