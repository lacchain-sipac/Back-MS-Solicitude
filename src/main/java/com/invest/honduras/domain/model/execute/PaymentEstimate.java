package com.invest.honduras.domain.model.execute;
import java.util.List;

import com.invest.honduras.domain.model.FileBean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentEstimate {
	
	@ApiModelProperty(notes = "Identificador de estimación de pago" , example = "541ba9ea-8221-4fa9-8f41-7865b76cf67a")	
	private String id;
	
	@ApiModelProperty(notes = "Solicitud de estimación de pago" , example = "")	
	private PaymentEstimateRequest request;
	
	@ApiModelProperty(notes = "Documento del tipo F01" , example = "")	
	private List<FileBean> f01;
	
	@ApiModelProperty(notes = "Documento CDP" , example = "")	
	private List<FileBean> proffPayment;
	
	@ApiModelProperty(notes = "datos" , example = "")	
	private String data;
}
