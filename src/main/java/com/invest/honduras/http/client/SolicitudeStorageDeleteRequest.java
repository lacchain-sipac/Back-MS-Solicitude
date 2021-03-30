package com.invest.honduras.http.client;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudeStorageDeleteRequest {
	@ApiModelProperty(notes = "Identificador de la solicitud" , example = "5f5970b686de650001c745fb")	
	private String idSolicitude;

	@ApiModelProperty(notes = "Identificador del documento" , example = "5f5970b686de650001c745fa")	
	private String idFile;

}
