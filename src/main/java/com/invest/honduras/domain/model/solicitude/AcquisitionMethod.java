package com.invest.honduras.domain.model.solicitude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcquisitionMethod  {
		
	@ApiModelProperty(notes = "Método de adquisión" , example = "11")	
	private String acquisitionMethod;
	
	@ApiModelProperty(notes = "Descripción del nuevo método" , example = "adquisión por terceros")	
	private String newMethod;
	
	@ApiModelProperty(notes = "Tipo de contrato" , example = "03")		
	private String contractType;
	
}
