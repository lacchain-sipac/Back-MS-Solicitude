package com.invest.honduras.domain.map;

import java.util.ArrayList;
import java.util.Date;

import com.invest.honduras.domain.model.FileBean;
import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.solicitude.Solicitude;
import com.invest.honduras.http.client.solicitude.SolicitudeUpdateRequest;
import com.invest.honduras.http.response.solicitude.SolicitudeItemResponse;

public class SolicitudeMap {

	public void mapping(Solicitude source, SolicitudeItemResponse destiny) {
		destiny.setId(source.getId());
		
		destiny.setIdProject(source.getIdProject());
		
		destiny.setDataProcess(source.getDataProcess());
		destiny.setFinancingLine(source.getFinancingLine());
		destiny.setAcquisitionMethod(source.getAcquisitionMethod());
		destiny.setDocument(source.getDocument());
		destiny.setBudgetStructure(source.getBudgetStructure());
		destiny.setCurrentStep(source.getCurrentStep());
		destiny.setAccredited(source.getAccredited());
		destiny.setFinishSolicitude(source.isFinishSolicitude());

	}

	public void mapping(SolicitudeUpdateRequest source, Solicitude destiny, UserSession userSession) {
		destiny.setDataProcess(source.getDataProcess());
		destiny.setFinancingLine(source.getFinancingLine());
		destiny.setAcquisitionMethod(source.getAcquisitionMethod());
		destiny.setDocument(source.getDocument());
		destiny.setBudgetStructure(source.getBudgetStructure());

		destiny.setCurrentStep(source.getCurrentStep());

		destiny.setFinishSolicitude(source.isFinishSolicitude());

		

		destiny.setLastModifiedBy(userSession.getEmail());
		destiny.setLastModifiedDate(new Date());

	}

	public void mappingInsert(Storage source, Solicitude destiny, UserSession userSession) {
		if (destiny.getDocument() == null) {
			destiny.setDocument(new ArrayList<>());
		}

		FileBean document = new FileBean();
		document.setIdStorage(source.getId());
		document.setDateCreation(new Date());
		document.setFileName(source.getFileName());
		document.setObservation(source.getObservation());
		document.setUser(userSession.getEmail());

		destiny.getDocument().add(document);

		destiny.setLastModifiedBy(userSession.getEmail());
		destiny.setLastModifiedDate(new Date());

	}

	public void mappingDelete(String idStorage, Solicitude destiny, UserSession userSession) {
		if (destiny.getDocument() != null) {

			destiny.getDocument().removeIf(b -> b.getIdStorage().equals(idStorage));

			destiny.setLastModifiedBy(userSession.getEmail());
			destiny.setLastModifiedDate(new Date());
		}
	}

	public void mapCreate(Solicitude destiny, UserSession userSession) {
		destiny.setCurrentStep(1);
		
		destiny.setDateAdmission(new Date());
		destiny.setCreatedBy(userSession.getEmail());
		destiny.setLastModifiedBy(userSession.getEmail());
		destiny.setCreatedDate(new Date());
		destiny.setLastModifiedDate(new Date());

	}

//	public void mapAccredit( Solicitude destiny, ApproveSolicitudeRequest request, String hash,
//			UserSession userSession) {
//		Accredited accredited = new Accredited();
//		accredited.setDate(new Date());
//		accredited.setHash(hash);
//		accredited.setObservation(request.getObservation());
//		accredited.setUser(userSession.getEmail());
//		accredited.setRole(userSession.getRole());
//
//		destiny.setAccredited(accredited);
//		
//		destiny.setLastModifiedBy(userSession.getEmail());
//		destiny.setLastModifiedDate(new Date());
//
//	}

}
