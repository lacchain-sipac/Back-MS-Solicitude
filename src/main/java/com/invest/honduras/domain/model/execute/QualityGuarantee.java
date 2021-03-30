package com.invest.honduras.domain.model.execute;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QualityGuarantee {
	
	@ApiModelProperty(notes = "Indica si tiene la calidad de garantía" , example = "true")	
	private boolean hasQualityGuarantee;
	
	@ApiModelProperty(notes = "Observación del cierre de Contrato" , example = "cerrando ciclo financiero")	
	private String observation;
	
	@ApiModelProperty(notes = "Indicador si la sección está acreditado" , example = "true")	
	private boolean accredited;
}
