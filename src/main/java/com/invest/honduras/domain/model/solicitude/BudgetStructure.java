package com.invest.honduras.domain.model.solicitude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetStructure {
	@ApiModelProperty(notes = "Tiene estructura presupuestaria" , example = "true")	
	private boolean viable;
	
	@ApiModelProperty(notes = "Cuenta con disponibilidad presupuestaria" , example = "true")	
	private boolean availabilityStructure;
	
	@ApiModelProperty(notes = "Estructura presupuestaria" , example = "345634563456")	
	private String budgetStructure;
	
	@ApiModelProperty(notes = "Observación" , example = "observar los precios")	
	private String additionalComment; 
}
