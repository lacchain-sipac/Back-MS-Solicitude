package com.invest.honduras.domain.model.process;

import java.util.List;

import com.invest.honduras.domain.model.Approved;
import com.invest.honduras.domain.model.FileBean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class DocumentApproveAccredited {
	
	@ApiModelProperty(notes = "Documento" , example = "")
	private FileBean document;
	
	@ApiModelProperty(notes = "Lista de revisiones del documento" , example = "")
	private List<Approved> review;
	
	@ApiModelProperty(notes = "Aprobación del documento" , example = "")
	private Approved approved; 
}
