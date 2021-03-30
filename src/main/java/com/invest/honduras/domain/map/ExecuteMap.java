package com.invest.honduras.domain.map;

import java.util.Date;

import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.execute.Execute;
import com.invest.honduras.http.client.ExecuteCreateRequest;
import com.invest.honduras.http.client.execute.ExecuteRequest;
import com.invest.honduras.http.response.execution.ExecuteItemResponse;
import com.invest.honduras.util.FlowConstants;

public class ExecuteMap {

	public void mappingCreate(String idProject, ExecuteCreateRequest request, Execute destiny, UserSession userSession) {
		destiny.setIdProject(idProject);
		destiny.setCurrentStep(1);
		destiny.setCreatedBy(userSession.getEmail());
		destiny.setCodeStep(request.getCodeStep());
		destiny.setCreatedDate(new Date());

	}

	public void mapping(Execute source, ExecuteItemResponse destiny) {
		if (source != null && destiny != null) {
			
			destiny.setId(source.getId());
			
			destiny.setIdProject(source.getIdProject());

			destiny.setAssignContractor(source.getAssignContractor());

			destiny.setPayment(source.getPayment());

			destiny.setModifiedContract(source.getModifiedContract());

			destiny.setQualityGuarantee(source.getQualityGuarantee());

			destiny.setCurrentStep(source.getCurrentStep());

			destiny.setFinishExecute(source.isFinishExecute());
			
			destiny.setCodeStep(source.getCodeStep());
		}
	}

	public void mapping(ExecuteRequest source, Execute destiny, UserSession userSession) {

		destiny.setCodeStep(source.getCodeStep());
		
		switch (source.getCodeStep()) {

		case FlowConstants.STEP_CONTRACT_SUPERVISOR:
			destiny.setAssignContractor(source.getAssignContractor());
			destiny.getAssignContractor().setAccredited(true);
			break;

		case FlowConstants.STEP_PAYMENT:
			destiny.setPayment(source.getPayment());
			destiny.getPayment().setAccredited(true);
			break;

		case FlowConstants.STEP_CONTRACT_MODIFICATE:
			destiny.setModifiedContract(source.getModifiedContract());
			destiny.getModifiedContract().setAccredited(true);
			break;

		case FlowConstants.STEP_QUALITY_GUARANTEE:
			destiny.setQualityGuarantee(source.getQualityGuarantee());
			destiny.getQualityGuarantee().setAccredited(true);

			break;
		}

		destiny.setCurrentStep(source.getCurrentStep());

		destiny.setLastModifiedBy(userSession.getEmail());

		destiny.setLastModifiedDate(new Date());

		destiny.setFinishExecute(source.isFinishExecute());
	}

}
