package com.invest.honduras.http.response.execution;

import com.invest.honduras.domain.model.execute.AssignContractor;
import com.invest.honduras.domain.model.execute.ModifiedContract;
import com.invest.honduras.domain.model.execute.PaymentManagement;
import com.invest.honduras.domain.model.execute.QualityGuarantee;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExecuteItemResponse {
	@ApiModelProperty(notes = "Indentificador de la ejecución", example = "5ecc3646fecab200012b6543")
	private String id;

	@ApiModelProperty(notes = "Inditificador del proyecto", example = "5f5970b686de650001c745fb")
	private String idProject;

	@ApiModelProperty(notes = "Asigna el contratista y supervisor" , example = "")	
	private AssignContractor assignContractor;

	@ApiModelProperty(notes = "Pagos en la ejecución del proyecto" , example = "")	
	private PaymentManagement payment;

	@ApiModelProperty(notes = "Modificación de contrato" , example = "")	
	private ModifiedContract modifiedContract;

	@ApiModelProperty(notes = "Garantía del calidad" , example = "")	
	private QualityGuarantee qualityGuarantee;

	@ApiModelProperty(notes = "Indica el paso o nivel de la ejecución" , example = "1")	
	private int currentStep;

	@ApiModelProperty(notes = "Indica si se ha finalizado el ejecución", example = "true")
	private boolean finishExecute;

	@ApiModelProperty(notes = "Indica el paso o nivel en el proyecto" , example = "paso_03_02")	
	private String codeStep;
}
