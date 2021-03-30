package com.invest.honduras.domain.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.invest.honduras.domain.model.execute.Execute;
import com.invest.honduras.domain.model.process.Process;
import com.invest.honduras.domain.model.solicitude.Solicitude;
import com.invest.honduras.model.BaseModel;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Document(collection = "project")
public class Project extends BaseModel{

	private static final long serialVersionUID = 2401203290973056930L;
	
	private String idAccredited;
	
	private Solicitude solicitude;
	private Process process;
	private Execute execute;
	private FinancialClosure financialClosure;
	private String currentStatus;
}
