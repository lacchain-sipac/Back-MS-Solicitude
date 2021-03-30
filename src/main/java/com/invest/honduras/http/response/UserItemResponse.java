package com.invest.honduras.http.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserItemResponse {
	@ApiModelProperty(notes = "Indentificador del usuario", example = "5ecc364-6fecab200-012b6543")
	private String id;
	
	@ApiModelProperty(notes = "Nombre del usuario", example = "Elmer")
	private String surnames;
	
	@ApiModelProperty(notes = "Apellido del usuario", example = "Lopez")
	private String fullname;
}
