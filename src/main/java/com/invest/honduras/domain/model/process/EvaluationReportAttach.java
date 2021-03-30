package com.invest.honduras.domain.model.process;

import java.util.List;

import com.invest.honduras.domain.model.FileBean;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class EvaluationReportAttach {

	/*ADD 23/04/2020 by rdelaros@everis.com */
	//private List<FileBean> document; 
	private List<DocumentApproveAccredited> document;
	/**/
	private boolean accredited;
}
