package com.invest.honduras.domain.model.execute;
import java.util.List;

import com.invest.honduras.domain.model.process.DocumentApproveAccredited;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifiedContract {
	@ApiModelProperty(notes = "Lista de documentos" , example = "")
	private List<DocumentApproveAccredited> document;
	
	@ApiModelProperty(notes = "Indicador si la sección está acreditado" , example = "true")	
	private boolean accredited;
}
