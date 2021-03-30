package com.invest.honduras.domain.model.execute;
import java.util.List;

import com.invest.honduras.domain.model.FileBean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinalPayment {
	
	@ApiModelProperty(notes = "Identificador de Pago final" , example = "517f8f6e-a978-442a-bfdf-3a94e0351d1b")	
	private String id;
	
	@ApiModelProperty(notes = "Requerimiento de Pago final" , example = "")	
	private FinalPaymentRequest request;
	
	@ApiModelProperty(notes = "Documento F01" , example = "")	
	private List<FileBean> f01;
	
	@ApiModelProperty(notes = "Documento CDP" , example = "")	
	private List<FileBean> proffPayment;
}
