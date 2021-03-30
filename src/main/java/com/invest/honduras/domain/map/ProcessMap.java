package com.invest.honduras.domain.map;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.invest.honduras.domain.model.User;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.process.Committee;
import com.invest.honduras.domain.model.process.Process;
import com.invest.honduras.domain.model.process.ProcessCommittee;
import com.invest.honduras.http.client.process.CommitteeRequest;
import com.invest.honduras.http.client.process.CommitteeUpdateRequest;
import com.invest.honduras.http.client.process.ProcessRequest;
import com.invest.honduras.http.response.process.ProcessItemResponse;
import com.invest.honduras.util.FlowConstants;

public class ProcessMap {

	public void mappingCreate(String idProject, Process destiny, UserSession userSession) {

		destiny.setIdProject(idProject);

		destiny.setCurrentStep(1);

		destiny.setCreatedBy(userSession.getEmail());

		destiny.setCreatedDate(new Date());

	}

	public void mapping(Process source, ProcessItemResponse destiny) {
		if (source != null && destiny != null) {

			destiny.setId(source.getId());

			destiny.setIdProject(source.getIdProject());

			destiny.setInit(source.getInit());

			destiny.setOpeningAct(source.getOpeningAct());

			destiny.setCommittee(source.getCommittee());

			destiny.setEvaluationReport(source.getEvaluationReport());

			destiny.setLoadStructure(source.getLoadStructure());

			destiny.setAwardResolution(source.getAwardResolution());

			destiny.setSignedContract(source.getSignedContract());

			destiny.setCurrentStep(source.getCurrentStep());

			destiny.setFinishProcess(source.isFinishProcess());

			destiny.setInitialized(source.isInitialized());
			
			destiny.setCodeStep(source.getCodeStep());

		}

	}

	public void mapping(ProcessRequest source, Process destiny, UserSession userSession) {


		destiny.setCodeStep(source.getCodeStep());
		
		switch (source.getCodeStep()) {

		case FlowConstants.STEP_START_PROCESS:
			destiny.setInit(source.getInit());
			destiny.getInit().setAccredited(true);
			break;

		case FlowConstants.STEP_OFFER_DOCUMENTATION:
			destiny.setOpeningAct(source.getOpeningAct());
			destiny.getOpeningAct().setAccredited(true);
			break;

		case FlowConstants.STEP_COMMITEE:
			destiny.setCommittee(source.getCommittee());
			destiny.getCommittee().setAccredited(true);
			break;

		case FlowConstants.STEP_EVALUATION_REPORT:
			destiny.setEvaluationReport(source.getEvaluationReport());
			destiny.getEvaluationReport().setAccredited(true);
			break;

		case FlowConstants.STEP_STRUCTURE_BUDGET:
			destiny.setLoadStructure(source.getLoadStructure());
			destiny.getLoadStructure().setAccredited(true);
			break;
			
		case FlowConstants.STEP_AWARD_RESOLUTION:
			destiny.setAwardResolution(source.getAwardResolution());
			destiny.getAwardResolution().setAccredited(true);
			break;
			
		case FlowConstants.STEP_SIGNED_CONTRACT:
			destiny.setSignedContract(source.getSignedContract());
			destiny.getSignedContract().setAccredited(true);
			destiny.setFinishProcess(source.isFinishProcess());
			break;
		}
		
		destiny.setCurrentStep(source.getCurrentStep());

		destiny.setLastModifiedBy(userSession.getEmail());

		destiny.setLastModifiedDate(new Date());

	}

	public void mappingUpdateCommittee(CommitteeUpdateRequest source, Process destiny, UserSession userSession) {
		if (destiny.getCommittee() != null && destiny.getCommittee().getCommittee() != null) {
			for (Committee item : destiny.getCommittee().getCommittee()) {
				if (source.getId().equals(item.getId())) {
					item.setUser(source.getUser());
					item.setDetailRole(source.getDetailRole());
					item.setRole(source.getRole());
				}
			}
			destiny.setCreatedBy(userSession.getEmail());
			destiny.setLastModifiedBy(userSession.getEmail());
		}
	}

	public void mappingDeleteCommittee(String idCommittee, Process destiny, UserSession userSession) {
		if (destiny.getCommittee() != null && destiny.getCommittee().getCommittee() != null) {
			destiny.getCommittee().getCommittee().removeIf(b -> b.getId().equals(idCommittee));

			destiny.setCreatedBy(userSession.getEmail());
			destiny.setLastModifiedBy(userSession.getEmail());
		}

	}

	public void mappingAddCommittee(CommitteeRequest source, Process destiny, UserSession userSession) {
		if (destiny.getCommittee() == null) {
			destiny.setCommittee(new ProcessCommittee());
		}

		if (destiny.getCommittee().getCommittee() == null) {
			destiny.getCommittee().setCommittee(new ArrayList<>());
		}
		Committee item = new Committee();
		item.setId(UUID.randomUUID().toString());
		item.setUser(source.getUser());
		item.setDetailRole(source.getDetailRole());
		item.setRole(source.getRole());

		destiny.getCommittee().getCommittee().add(item);

	}

	public void initProcess(Process process, User user, String codeStep) {
		process.setInitialized(true);
		process.setCodeStep(codeStep);

		process.setLastModifiedBy(user.getFullname());

		process.setLastModifiedDate(new Date());
	}

}
