package com.invest.honduras.domain.model.process;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitPreparationDocument {
	
	private List<DocumentApproveAccredited> document;
	private boolean accredited;
	
}