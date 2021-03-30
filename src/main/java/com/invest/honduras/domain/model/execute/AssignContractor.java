package com.invest.honduras.domain.model.execute;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignContractor {
	@ApiModelProperty(notes = "Lista de identificadores de contratistas" , example = "5e6a7d9e2d518c2f700decc1")	
	private List<String> contractor;
	
	@ApiModelProperty(notes = "Lista de identificadores de supervisores" , example = "5e6a7d9e2d518c2f700decc1")	
	private List<String> supervising;
	
	@ApiModelProperty(notes = "Indicador si la sección está acreditado" , example = "true")	
	private boolean accredited;
}
