package com.invest.honduras.domain.model.execute;
import java.util.List;

import com.invest.honduras.domain.model.process.DocumentApproveAccredited;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdvanceRequest {
	@ApiModelProperty(notes = "Documento de anticipo" , example = "")
	private List<DocumentApproveAccredited> document;
	
	@ApiModelProperty(notes = "Indicador del documento es acreditado" , example = "true")
	private boolean accredited;
}
