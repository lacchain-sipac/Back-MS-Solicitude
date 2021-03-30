package com.invest.honduras.domain.model.solicitude;

import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "status_solicitude")
public class StatusSolicitude {
	@ApiModelProperty(notes = "codigo del estado", example = "L")
	private String code;
	
	@ApiModelProperty(notes = "Nombre del estado", example = "En Licitacion")
	private String name;

}
