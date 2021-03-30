package com.invest.honduras.domain.model.solicitude;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SolicitudeProcess {
	
	@ApiModelProperty(notes = "Objeto de la transacción" , example = "01")	
	private String transactionType;

	@ApiModelProperty(notes = "Nombre del proceso" , example = "Proceso de compra de carros")	
	private String processName;
	
	@ApiModelProperty(notes = "Lista de procesos" , example = "")	
	private List<SolicitudeProcessDetail> processDetail;
}
