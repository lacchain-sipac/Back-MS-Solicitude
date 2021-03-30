package com.invest.honduras.http.client;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectFinishRequest {
	
	@ApiModelProperty(notes = "Indica el paso o nivel en el proyecto" , example = "fase_option_03_04")	
	private String codeStep;
}
