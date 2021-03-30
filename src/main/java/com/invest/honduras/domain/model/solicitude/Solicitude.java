package com.invest.honduras.domain.model.solicitude;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.invest.honduras.domain.model.Accredited;
import com.invest.honduras.domain.model.FileBean;
import com.invest.honduras.model.BaseModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "solicitude")
public class Solicitude extends BaseModel {

	private static final long serialVersionUID = 907439410159356132L;

	private String idProject;
	
	private SolicitudeProcess dataProcess;

	private FinancingLine financingLine;

	private AcquisitionMethod acquisitionMethod;

	private List<FileBean> document;

	private BudgetStructure budgetStructure;

	private int currentStep;
	
	private boolean finishSolicitude;
	
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date dateAdmission;
    
    private Accredited accredited;
    
}
