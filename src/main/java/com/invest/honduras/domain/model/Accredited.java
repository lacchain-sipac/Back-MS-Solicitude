package com.invest.honduras.domain.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Accredited {
	@ApiModelProperty(notes = "Indica el rol del usuario", example = "ROLE_DIR_ADJ")
	private String role;
	@ApiModelProperty(notes = "Nombre del usuario que acredita la solicitud", example = "Margaret Perez")
	private String user;
	
	@ApiModelProperty(notes = "Identificador del usuario que acredito la solicitud en la blockchain", example = "0x05281bc91a2ce82d2986f9a13765731c84507d65")
	private String proxy;
	
	@ApiModelProperty(notes = "hash de los campos de la solicitud que será empleado como token", example = "0xe99998f50b2b69b52e4574b1a829fce8ce47d1596b8756275257c50e1000c4c7")
	private String hash;

	@ApiModelProperty(notes = "Observación de la acreditación de la solicitud", example = "falta de recursos")
	private String observation;

	@ApiModelProperty(notes = "Fecha de acreditacón", example = "2020-09-10T02:38:57.931+0000")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date date;
}
