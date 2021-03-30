package com.invest.honduras.domain.model.execute;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentManagement {
	@ApiModelProperty(notes = "Pagos por anticipo" , example = "")	
	private List<Advance> advance;
	
	@ApiModelProperty(notes = "Pago estimado" , example = "")	
	private List<PaymentEstimate> estimate;
	
	@ApiModelProperty(notes = "Pago final" , example = "")	
	private List<FinalPayment> finalPayment;
	
	@ApiModelProperty(notes = "Modificación del contrato" , example = "")	
	private ModifiedContract modifiedContract;
	
	@ApiModelProperty(notes = "Indicador si la sección está acreditado" , example = "true")	
	private boolean accredited;
}
