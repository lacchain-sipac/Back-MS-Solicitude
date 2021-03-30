package com.invest.honduras.domain.model.vc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.invest.honduras.domain.model.FileBean;
import com.invest.honduras.domain.model.solicitude.FinancingLine;
import com.invest.honduras.domain.model.solicitude.Solicitude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialSubject {
	private String id;
	private SolicitudeProcess address;
	private FinancialProduct financialProduct;
	private AcquisitionMethod acquisitionMethod;
	private List<Documentation> documentation = new ArrayList<>();
	private BudgetStructure budget;

	public CredentialSubject(Solicitude s) {
		id = "urn:id:" + s.getId();
		address = new SolicitudeProcess(s.getDataProcess());
		financialProduct = new FinancialProduct(s.getFinancingLine());
		acquisitionMethod = new AcquisitionMethod(s.getAcquisitionMethod());
		if (s.getDocument() != null)
			s.getDocument().forEach(d -> {
				documentation.add(new Documentation(d));
			});
		budget = new BudgetStructure(s.getBudgetStructure());
	}
}

@Getter
@Setter
class SolicitudeProcess {
	private String legalName;
	private String additionalType;
	private List<SolicitudeProcessDetail> ownershipFundingInfo = new ArrayList<>();

	public SolicitudeProcess(com.invest.honduras.domain.model.solicitude.SolicitudeProcess s) {
		legalName = s.getProcessName();
		additionalType = s.getTransactionType();
		if (s.getProcessDetail() != null)
			s.getProcessDetail().forEach(i -> {
				ownershipFundingInfo.add(new SolicitudeProcessDetail(i));
			});

	}

}

@Getter
@Setter
class SolicitudeProcessDetail {
	private String fundingSource;
	private String operationNumber;
	private String associatedComponent;
	private String contribution;

	public SolicitudeProcessDetail(com.invest.honduras.domain.model.solicitude.SolicitudeProcessDetail s) {
		fundingSource = s.getFundingSource();
		operationNumber = s.getOperationNumber();
		associatedComponent = s.getAssociatedComponent();
		contribution = s.getContribution();
	}
}

@Getter
@Setter
class FinancialProduct {
	private String pacLine;
	private String amountOfThisGood;
	private String unitText;
	private String description;
	private Amount price;

	public FinancialProduct(FinancingLine f) {
		pacLine = f.getPacLine();
		amountOfThisGood = f.getQuantity();
		unitText = f.getUnit();
		description = f.getDescription();
		price = new Amount(f.getEstimatedAmount());
	}
}

@Getter
@Setter
class Amount {
	private String priceCurrency;
	private String amount;

	public Amount(com.invest.honduras.domain.model.solicitude.Amount a) {
		this.amount = a.getAmount();
		this.priceCurrency = a.getCode();
	}
}

@Getter
@Setter
class AcquisitionMethod {

	private String method;
	private String newMethod;
	private String contractType;

	public AcquisitionMethod(com.invest.honduras.domain.model.solicitude.AcquisitionMethod a) {
		method = a.getAcquisitionMethod();
		newMethod = a.getNewMethod();
		contractType = a.getContractType();
	}
}

@Getter
@Setter
class Documentation {

	private String name;
	private String author;
	private Date dateCreated;
	private String comment;

	public Documentation(FileBean file) {
		name = file.getFileName();
		author = file.getUser();
		dateCreated = file.getDateCreation();
		comment = file.getObservation();
	}
}

@Getter
@Setter
class BudgetStructure {

	private boolean hasViable;
	private boolean hasBudget;
	private String textBudget;
	private String comment;

	public BudgetStructure(com.invest.honduras.domain.model.solicitude.BudgetStructure b) {
		hasViable = b.isViable();
		hasBudget = b.isAvailabilityStructure();
		textBudget = b.getBudgetStructure();
		comment = b.getAdditionalComment();
	}
}
