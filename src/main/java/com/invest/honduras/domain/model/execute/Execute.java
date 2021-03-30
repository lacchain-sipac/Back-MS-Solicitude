package com.invest.honduras.domain.model.execute;

import org.springframework.data.mongodb.core.mapping.Document;

import com.invest.honduras.model.BaseModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "execute")
public class Execute extends BaseModel {

	private static final long serialVersionUID = 3088362848488385546L;

	private String idProject;

	private AssignContractor assignContractor;

	private PaymentManagement payment;

	private ModifiedContract modifiedContract;

	private QualityGuarantee qualityGuarantee;

	private int currentStep;

	private boolean finishExecute;
	
	private String codeStep;

}
