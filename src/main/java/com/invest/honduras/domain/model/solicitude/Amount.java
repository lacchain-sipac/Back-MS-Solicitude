package com.invest.honduras.domain.model.solicitude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Amount {

	@ApiModelProperty(notes = "Simbolo de moneda" , example = "USD")
	private String code;
	@ApiModelProperty(notes = "Monto estimado" , example = "12.00")
	private String amount;
}
