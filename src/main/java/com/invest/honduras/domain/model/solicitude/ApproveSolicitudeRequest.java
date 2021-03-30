package com.invest.honduras.domain.model.solicitude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproveSolicitudeRequest {
	@ApiModelProperty(notes = "Identificador de la solicitud" , example = "5f5970b686de650001c745fa")	
	private String idSolicitude;
	@ApiModelProperty(notes = "Observación de la NO acreditación de la solicitud", example = "falta de recursos")
	private String observation;
	
	@ApiModelProperty(notes = "Indica el paso o nivel de la solicitud" , example = "5")	
	private String codeStep;
	
	@ApiModelProperty(notes = "Indica si se acredita o No una solicitud", example = "true")	
	private boolean accredited;
}
