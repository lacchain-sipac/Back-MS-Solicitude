package com.invest.honduras.domain.model.solicitude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudeProcessDetail {

	@ApiModelProperty(notes = "Fuente de financiamiento" , example = "03")	
	private String fundingSource;
	
	@ApiModelProperty(notes = "Número y nombre de la operación" , example = "3815/BL-HO")	
	private String operationNumber;
	
	@ApiModelProperty(notes = "Componente asociado" , example = "05")	
	private String associatedComponent;
	
	@ApiModelProperty(notes = "Organismo financiero" , example = "01")	
	private String contribution;
}
