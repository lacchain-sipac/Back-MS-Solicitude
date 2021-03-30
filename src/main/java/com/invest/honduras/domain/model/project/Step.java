package com.invest.honduras.domain.model.project;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Step {

	private String code;
	
	private List<DocumentType> documentType;
}
