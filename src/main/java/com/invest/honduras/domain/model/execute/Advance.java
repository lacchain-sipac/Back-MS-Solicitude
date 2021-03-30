package com.invest.honduras.domain.model.execute;
import java.util.List;

import com.invest.honduras.domain.model.FileBean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Advance {
	@ApiModelProperty(notes = "Identificador de anticipo" , example = "74839491-fafe-4ade-91ec-0c08cce3406f")	
	private String id;
	
	@ApiModelProperty(notes = "solicitud de anticipo" , example = "")	
	private AdvanceRequest request;
	
	@ApiModelProperty(notes = "Documento de tipo f01" , example = "")	
	private List<FileBean> f01;
	
	@ApiModelProperty(notes = "Documento CDP Anticipo" , example = "")	
	private List<FileBean> proffPayment;
}
