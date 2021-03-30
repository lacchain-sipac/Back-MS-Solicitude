package com.invest.honduras.domain.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileBean {
	@ApiModelProperty(notes = "identificador del grupo del documento" , example =  "5f59896186de650001c745fc")
	private String idGroup;
	
	@ApiModelProperty(notes = "nombre del documento" , example =  "Reconciliacion.pdf")
	private String fileName;
	
	@ApiModelProperty(notes = "identificador del usuario que ingresa el documento" , example =  "emma ardiles")
	private String user;
	
	@ApiModelProperty(notes = "compañia" , example =  "EVERIS.SAC")
	private String company;
	
	
	@ApiModelProperty(notes = "fecha de creación" , example =  "2020-09-10T02:03:13.817+0000")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date dateCreation;
	
	@ApiModelProperty(notes = "observación del documento" , example =  "documento de referencia")
	private String observation;
	
	@ApiModelProperty(notes = "identificador del documento" , example =  "5f59896186de650001c745fc")
	private String idStorage;
	
	@ApiModelProperty(notes = "hash del documento hacia la blockchain" , example =  "0x98945003982394823498234")
	private String hash;
	
	@ApiModelProperty(notes = "firma del contrato" , example =  "43452352345")
	private String signedContract;

}
