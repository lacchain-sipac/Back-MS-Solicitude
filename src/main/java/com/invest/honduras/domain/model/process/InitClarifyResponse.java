package com.invest.honduras.domain.model.process;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class InitClarifyResponse {
	private boolean accredited;
	private List<DocumentApproveAccredited> document; 
}
