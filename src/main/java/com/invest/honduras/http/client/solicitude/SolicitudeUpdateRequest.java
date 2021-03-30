package com.invest.honduras.http.client.solicitude;

import java.util.List;

import com.invest.honduras.domain.model.FileBean;
import com.invest.honduras.domain.model.solicitude.AcquisitionMethod;
import com.invest.honduras.domain.model.solicitude.BudgetStructure;
import com.invest.honduras.domain.model.solicitude.FinancingLine;
import com.invest.honduras.domain.model.solicitude.SolicitudeProcess;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudeUpdateRequest {
	@ApiModelProperty(notes = "Identificador del proyecto" , example = "5f5970b686de650001c745fa")	
	private String idProject;
	
	@ApiModelProperty(notes = "Datos del proceso" , example = "")	
	private SolicitudeProcess dataProcess;

	@ApiModelProperty(notes = "Plan de adquisiciones" , example = "")	
	private FinancingLine financingLine;

	@ApiModelProperty(notes = "Método de adquisión y tipo de contrato" , example = "")	
	private AcquisitionMethod acquisitionMethod;

	@ApiModelProperty(notes = "Documentación técnica" , example = "")	
	private List<FileBean> document;
	
	@ApiModelProperty(notes = "Disponibilidad y estructura presupuestaria" , example = "")	
	private BudgetStructure budgetStructure;

	@ApiModelProperty(notes = "Indica el paso o nivel del proceso" , example = "1")	
	private int currentStep;
	
	@ApiModelProperty(notes = "Indica si la solicitud ha terminado" , example = "false")	
	private boolean finishSolicitude;


}
