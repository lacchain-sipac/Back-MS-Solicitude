package com.invest.honduras.domain.model.process;

import java.util.List;

import com.invest.honduras.domain.model.FileBean;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class EvaluationReport {

	private EvaluationReportAttach evaluation; 
	private List<FileBean> noObjection;
	private List<FileBean> responseNoObjection;
	private boolean accredited;
}
