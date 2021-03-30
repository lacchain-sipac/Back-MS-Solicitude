package com.invest.honduras.domain.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Approved { 
	
	@ApiModelProperty(notes = "Usuario que revisa o aprueba" , example = "Emma ardiles - Director de Transporte")
	private String user; 
	
	@ApiModelProperty(notes = "Observación del documento" , example = "acreditando")
	private String observation;
	
	@ApiModelProperty(notes = "tipo de revisión o aprobación del documento" , example = "accredit")
	private String type;
	
	@ApiModelProperty(notes = "Fecha de revisión o aprobación del documento" , example = "2020-07-07T00:01:25.219+0000")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date date;
	
	@ApiModelProperty(notes = "Hash del documento" , example = "0xaa60a5d6765e74771935981d48c0db0e3ddc6a2ec9390af3f7f75f5225b06d5a")
	private String hash;
}
