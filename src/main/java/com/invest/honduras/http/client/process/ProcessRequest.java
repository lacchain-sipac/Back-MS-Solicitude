package com.invest.honduras.http.client.process;

import com.invest.honduras.domain.model.process.AwardResolution;
import com.invest.honduras.domain.model.process.EvaluationReport;
import com.invest.honduras.domain.model.process.OpeningAct;
import com.invest.honduras.domain.model.process.ProcessCommittee;
import com.invest.honduras.domain.model.process.ProcessInit;
import com.invest.honduras.domain.model.process.ProcessLoadStructureBudget;
import com.invest.honduras.domain.model.process.ProcessSignedContract;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessRequest  {

	private String idProject;
	
	private ProcessInit init;
	
	private OpeningAct openingAct;
	
	private ProcessCommittee committee;
	
	private EvaluationReport evaluationReport;
	
	private ProcessLoadStructureBudget loadStructure;

	private  AwardResolution awardResolution;
	
	private ProcessSignedContract signedContract;
	
	private int currentStep;
	
	private boolean finishProcess;
	
	private String codeStep;

}
