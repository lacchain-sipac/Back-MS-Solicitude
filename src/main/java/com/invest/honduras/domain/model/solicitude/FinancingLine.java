package com.invest.honduras.domain.model.solicitude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinancingLine {
	@ApiModelProperty(notes = "Línea PAC Nº" , example = "1003029")	
	private String pacLine;
	
	@ApiModelProperty(notes = "Cantidad" , example = "12")	
	private String quantity;
	
	@ApiModelProperty(notes = "Unidad" , example = "01")	
	private String unit;
	
	@ApiModelProperty(notes = "Descripción" , example = "compra de autos")	
	private String description;
	
	@ApiModelProperty(notes = "Monto estimado" , example = "USD 12")	
	private Amount estimatedAmount;
}
