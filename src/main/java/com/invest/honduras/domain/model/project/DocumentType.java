package com.invest.honduras.domain.model.project;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentType {

	private String code;
	private String name;
	private String description;
	private List<DocumentTypeAssociated> documentTypeAssociated;
}

