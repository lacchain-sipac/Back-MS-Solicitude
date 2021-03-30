package com.invest.honduras.domain.model.process;

import org.springframework.data.mongodb.core.mapping.Document;

import com.invest.honduras.model.BaseModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "process")
public class Process extends BaseModel {

	private static final long serialVersionUID = -903174915352155085L;

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

	private boolean initialized;
	
	private String codeStep;
}
