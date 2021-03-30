package com.invest.honduras.dao;

import java.util.List;

import com.invest.honduras.domain.model.solicitude.Parameter;
import com.invest.honduras.domain.model.solicitude.StatusSolicitude;

public interface ParameterDao {

	List<Parameter> listParameter();
	
	List<StatusSolicitude> listStatusSolicitude();
}
