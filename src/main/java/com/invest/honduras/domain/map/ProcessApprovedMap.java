package com.invest.honduras.domain.map;

import java.util.ArrayList;
import java.util.Date;

import com.invest.honduras.domain.model.Approved;
import com.invest.honduras.domain.model.User;
import com.invest.honduras.domain.model.process.AwardResolution;
import com.invest.honduras.domain.model.process.DocumentApproveAccredited;
import com.invest.honduras.domain.model.process.EvaluationReport;
import com.invest.honduras.domain.model.process.Process;
import com.invest.honduras.domain.model.process.ProcessLoadStructureBudget;
import com.invest.honduras.domain.model.process.ProcessSignedContract;
import com.invest.honduras.http.client.process.ApproveRequest;
import com.invest.honduras.service.impl.ProcessServiceImpl;
import com.invest.honduras.util.FlowConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessApprovedMap {

	public void mapping(ApproveRequest request, String hash, Process destiny, User user,
			boolean accredited) {

		String idStorage = request.getIdStorage();
		
		
		Approved approved = new Approved();
		approved.setHash(hash);
		
		/*RDELAROSA ADD ROLE 06/06/2020 */
		log.info("PROCESS - USER - ROL :"+request.getRole());
		log.info("PROCESS - USER - ROLNAME :"+request.getRoleName());
		
		approved.setUser(String.format("%s %s", user.getFullname(), user.getSurnames())+" - " + request.getRoleName());
		
		approved.setObservation(request.getObservation());
		approved.setDate(new Date());
		approved.setType(request.getType());

		destiny.setLastModifiedBy(user.getEmail());
		destiny.setLastModifiedDate(new Date());

		switch (request.getCodeStep()) {
		case FlowConstants.STEP_START_PROCESS:

			if (FlowConstants.DOC_BIDDING_ATTACH.equals(request.getDocumentType()))
				attachBidding(idStorage, destiny, approved, request, accredited);

			if (FlowConstants.DOC_CLARIFICATION_RESPONSE.equals(request.getDocumentType()))
				attachClarificationResponse(idStorage, destiny, approved, request, accredited);

			if (FlowConstants.DOC_AMENDMENT_RESPONSE.equals(request.getDocumentType()))
				attachAmendmentResponse(idStorage, destiny, approved, request, accredited);
			

			break;
	    
		/* ***********************ADD RDELAROSA 07/05/2020 ***************************/
			
		case FlowConstants.STEP_EVALUATION_REPORT:
			
			
			if(FlowConstants.DOC_EVALUATION_REPORT.equals(request.getDocumentType())) {
				System.out.println("Entro por aca a mapping.....");
				
				log.info("ProccesApprovedMap + antes de entrar a attachEvaluationReport...");
				
				attachEvaluationReport(idStorage, destiny, approved, request, accredited);
			}
			
			break;
			
		case FlowConstants.STEP_SIGNED_CONTRACT:	

			if(FlowConstants.DOC_SIGNED_CONTRACT.equals(request.getDocumentType())) {
				
				log.info("ProccesApprovedMap + antes de entrar a attachSignedContract...");
				
				attachSignedContract(idStorage, destiny, approved, request, accredited);
				
			}	
			
			break;
			
			
		/***************************************************************************/	

		default:
			log.error("No entro a ningun Case");
			break;
		}

	}

	private void attachBidding(String idStorage, Process destiny, Approved appoved, ApproveRequest request,
			boolean accredited) {

		if (destiny.getInit() != null && destiny.getInit().getPreparation() != null
				&& destiny.getInit().getPreparation().getBidding() != null
				&& destiny.getInit().getPreparation().getBidding().getDocument() != null)
			for (DocumentApproveAccredited document : destiny.getInit().getPreparation().getBidding().getDocument()) {

				if (idStorage.equals(document.getDocument().getIdStorage())) {
					request.setNameFile(document.getDocument().getFileName());

					if (accredited) {
						document.setApproved(appoved);
					} else {
						if (document.getReview() == null)
							document.setReview(new ArrayList<>());
						document.getReview().add(appoved);
					}

				}

			}
		
	}

	private void attachClarificationResponse(String idStorage, Process destiny, Approved appoved,
			ApproveRequest request, boolean accredited) {

		if (destiny.getInit() != null && destiny.getInit().getClarify() != null)

			destiny.getInit().getClarify().forEach(item -> {

				if (item.getResponse() != null && item.getResponse().getDocument() != null) {

					item.getResponse().getDocument().forEach(doc -> {
						if (idStorage.equals(doc.getDocument().getIdStorage())) {
							request.setNameFile(doc.getDocument().getFileName());
							if (accredited) {
								doc.setApproved(appoved);
							} else {
								if (doc.getReview() == null)
									doc.setReview(new ArrayList<>());
								doc.getReview().add(appoved);
							}
						}
					});
				}
			});
	}

	private void attachAmendmentResponse(String idStorage, Process destiny, Approved appoved, ApproveRequest request,
			boolean accredited) {

		if (destiny.getInit() != null && destiny.getInit().getAmendment() != null)

			destiny.getInit().getAmendment().forEach(item -> {

				if (item.getResponse() != null && item.getResponse().getDocument() != null)

					item.getResponse().getDocument().forEach(document -> {
						if (idStorage.equals(document.getDocument().getIdStorage())) {
							request.setNameFile(document.getDocument().getFileName());
							if (accredited) {
								document.setApproved(appoved);
							} else {
								if (document.getReview() == null)
									document.setReview(new ArrayList<>());
								document.getReview().add(appoved);
							}
						}
					});
			});

	}
	
	/* ***********************ADD RDELAROSA 07/05/2020 ***************************/
	
	private void attachEvaluationReport(String idStorage, Process destiny, Approved appoved, ApproveRequest request,
			boolean accredited) {

		log.info("ProccesApprovedMap + ENtro a attachEvaluationReport...");
		
		if (destiny.getEvaluationReport() != null && destiny.getEvaluationReport().getEvaluation() != null) {
			for (DocumentApproveAccredited document : destiny.getEvaluationReport().getEvaluation().getDocument()) {
				
				log.info("ProccesApprovedMap + ENtro a attachEvaluationReport..."+document.getDocument().getFileName());
				
				if (idStorage.equals(document.getDocument().getIdStorage())) {
					request.setNameFile(document.getDocument().getFileName());
					if (accredited) {
						
						log.info("Entro a acreditar");
						log.info("Entro a acreditar"+appoved.getObservation());
						log.info("Entro a acreditar"+appoved.getType());
						log.info("Entro a acreditar"+appoved.getUser());
						
						document.setApproved(appoved);
					} else {
						
						log.info("Entro a revisar");
						log.info("Entro a revisar"+appoved.getObservation());
						log.info("Entro a revisar"+appoved.getType());
						log.info("Entro a revisar"+appoved.getUser());
						
						if (document.getReview() == null)
							document.setReview(new ArrayList<>());
						document.getReview().add(appoved);
					}
				}
			}
		
		}
		
		
		log.info("ProccesApprovedMap + Saliendo de attachEvaluationReport...");
		
		

	}
	
	private void attachSignedContract(String idStorage, Process destiny, Approved appoved, ApproveRequest request,
			boolean accredited) {


		if (destiny.getSignedContract() != null && destiny.getSignedContract().getContract() != null)

			destiny.getSignedContract().getContract().forEach(item -> {

				if (item.getDocument() != null)

					item.getDocument().forEach(document -> {
						if (idStorage.equals(document.getDocument().getIdStorage())) {
							request.setNameFile(document.getDocument().getFileName());
							if (accredited) {
								document.setApproved(appoved);
							} else {
								if (document.getReview() == null)
									document.setReview(new ArrayList<>());
								document.getReview().add(appoved);
							}
						}
					});
			});

	}
	
	/***************************************************************************************************************/

}
