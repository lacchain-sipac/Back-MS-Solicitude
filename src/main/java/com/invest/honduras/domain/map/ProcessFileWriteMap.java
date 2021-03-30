package com.invest.honduras.domain.map;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.invest.honduras.domain.model.FileBean;
import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.domain.model.User;
import com.invest.honduras.domain.model.process.AwardResolution;
import com.invest.honduras.domain.model.process.Contract;
import com.invest.honduras.domain.model.process.DocumentApproveAccredited;
import com.invest.honduras.domain.model.process.EvaluationReport;
import com.invest.honduras.domain.model.process.EvaluationReportAttach;
import com.invest.honduras.domain.model.process.FlowFile;
import com.invest.honduras.domain.model.process.InitAmendment;
import com.invest.honduras.domain.model.process.InitAmendmentResponse;
import com.invest.honduras.domain.model.process.InitClarify;
import com.invest.honduras.domain.model.process.InitClarifyResponse;
import com.invest.honduras.domain.model.process.InitPreparation;
import com.invest.honduras.domain.model.process.InitPreparationDocument;
import com.invest.honduras.domain.model.process.OpeningAct;
import com.invest.honduras.domain.model.process.Process;
import com.invest.honduras.domain.model.process.ProcessInit;
import com.invest.honduras.domain.model.process.ProcessSignedContract;
import com.invest.honduras.util.FlowConstants;

public class ProcessFileWriteMap {

	public void mappingFileInsert( Process destiny, FlowFile flowFile,  Storage source, User user) {

		FileBean fileBean = new FileBean();
		fileBean.setIdStorage(source.getId());
		fileBean.setDateCreation(new Date());
		fileBean.setFileName(source.getFileName());
		fileBean.setObservation(source.getObservation());
		fileBean.setUser(String.format("%s %s", user.getFullname(), user.getSurnames()));

		fileBean.setHash(source.getHash());
		fileBean.setIdGroup(flowFile.getIdGroup() == null ? "" : flowFile.getIdGroup());
		destiny.setLastModifiedBy(user.getEmail());

		destiny.setLastModifiedDate(new Date());
		String documentType = flowFile.getDocumentType();
		boolean accredited = flowFile.isAccredited();

		switch (flowFile.getCodeStep()) {

		case FlowConstants.STEP_START_PROCESS:

			if (FlowConstants.DOC_BIDDING_ATTACH.equals(documentType))
				attachBidding(destiny, fileBean, accredited);

			if (FlowConstants.DOC_BIDDING_NO_OBJECTION.equals(documentType))
				attachBiddingNoObjection(destiny, fileBean);

			if (FlowConstants.DOC_BIDDING_RESPONSE_NO_OBJECTION.equals(documentType))
				attachBiddingResponseNoObjection(destiny, fileBean);

			if (FlowConstants.DOC_CLARIFICATION_REQUEST_.equals(documentType))
				attachClarificationRequest(destiny, fileBean);

			if (FlowConstants.DOC_CLARIFICATION_RESPONSE.equals(documentType))
				attachClarificationResponse(destiny, fileBean, accredited);

			if (FlowConstants.DOC_AMENDMENT_REQUEST.equals(documentType))
				attachAmendmentRequest(destiny, fileBean);

			if (FlowConstants.DOC_AMENDMENT_RESPONSE.equals(documentType))
				attachAmendmentResponse(destiny, fileBean, accredited);

			if (FlowConstants.DOC_AMENDMENT_NO_OBJECTION.equals(documentType))
				attachAmendmentNoObjection(destiny, fileBean);

			if (FlowConstants.DOC_AMENDMENT_RESPONSE_NO_OBJECTION.equals(documentType))
				attachAmendmentResponseNoObjection(destiny, fileBean);
			break;

		case FlowConstants.STEP_OFFER_DOCUMENTATION:
			if (FlowConstants.DOC_ACT_OPPENING.equals(documentType))
				attachActOppening(destiny, fileBean);
			break;

		case FlowConstants.STEP_EVALUATION_REPORT:
			if (FlowConstants.DOC_EVALUATION_REPORT.equals(documentType))
				attachEvaluationReport(destiny, fileBean, accredited);

			if (FlowConstants.DOC_EVALUATION_REPORT_NO_OBJECTION.equals(documentType))
				attachEvaluationReportNoObjection(destiny, fileBean);

			if (FlowConstants.DOC_EVALUATION_REPORT_RESPONSE_NO_OBJECTION.equals(documentType))
				attachEvaluationReportResponseNoObjection(destiny, fileBean);
			break;

		case FlowConstants.STEP_AWARD_RESOLUTION:
			if (FlowConstants.DOC_AWARD_RESOLUTION.equals(documentType))
				/* QUIT RDELAROSA 12/06/2020 */
				//attachAwardResolution(destiny, fileBean, accredited);
			    /* ADD RDELAROSA 12/06/2020 */
				attachAwardResolution(destiny, fileBean);
			break;

		case FlowConstants.STEP_SIGNED_CONTRACT:
			if (FlowConstants.DOC_SIGNED_CONTRACT.equals(documentType))
				attachSignedContract(destiny, fileBean, accredited);
			break;
		default:
			break;
		}

	}

	private void attachBidding(Process destiny, FileBean fileDocument, boolean accredited) {

		DocumentApproveAccredited documentAproveAccredited = new DocumentApproveAccredited();
		documentAproveAccredited.setDocument(fileDocument);

		if (destiny.getInit() == null)
			destiny.setInit(new ProcessInit());

		if (destiny.getInit().getPreparation() == null)
			destiny.getInit().setPreparation(new InitPreparation());

		if (destiny.getInit().getPreparation().getBidding() == null)
			destiny.getInit().getPreparation().setBidding(new InitPreparationDocument());

		if (destiny.getInit().getPreparation().getBidding().getDocument() == null)
			destiny.getInit().getPreparation().getBidding().setDocument(new ArrayList<>());

		destiny.getInit().getPreparation().getBidding().setAccredited(accredited);
		destiny.getInit().getPreparation().getBidding().getDocument().add(documentAproveAccredited);

	}

	private void attachBiddingNoObjection(Process destiny, FileBean fileDocument) {
		if (destiny.getInit() == null)
			destiny.setInit(new ProcessInit());

		if (destiny.getInit().getPreparation() == null)
			destiny.getInit().setPreparation(new InitPreparation());

		if (destiny.getInit().getPreparation().getNoObjection() == null)
			destiny.getInit().getPreparation().setNoObjection(new ArrayList<>());

		destiny.getInit().getPreparation().getNoObjection().add(fileDocument);

	}

	private void attachBiddingResponseNoObjection(Process destiny, FileBean fileDocument) {
		if (destiny.getInit() == null)
			destiny.setInit(new ProcessInit());

		if (destiny.getInit().getPreparation() == null)
			destiny.getInit().setPreparation(new InitPreparation());

		if (destiny.getInit().getPreparation().getResponseNoObjection() == null)
			destiny.getInit().getPreparation().setResponseNoObjection(new ArrayList<>());

		destiny.getInit().getPreparation().getResponseNoObjection().add(fileDocument);

	}

	private void attachClarificationRequest(Process destiny, FileBean fileDocument) {
		if (destiny.getInit() == null)
			destiny.setInit(new ProcessInit());

		if (destiny.getInit().getClarify() == null)
			destiny.getInit().setClarify(new ArrayList<>());

		InitClarify clarify = destiny.getInit().getClarify().stream()
				.filter(item -> fileDocument.getIdGroup().equals(item.getId())).findAny().orElse(null);

		if (clarify == null) {
			clarify = new InitClarify();
			clarify.setId(UUID.randomUUID().toString());
			destiny.getInit().getClarify().add(clarify);
		}

		if (clarify.getRequest() == null)
			clarify.setRequest(new ArrayList<>());

		clarify.getRequest().add(fileDocument);

	}

	private void attachClarificationResponse(Process destiny, FileBean fileDocument, boolean accredited) {
		DocumentApproveAccredited documentAproveAccredited = new DocumentApproveAccredited();
		documentAproveAccredited.setDocument(fileDocument);

		if (destiny.getInit() == null)
			destiny.setInit(new ProcessInit());

		if (destiny.getInit().getClarify() == null)
			destiny.getInit().setClarify(new ArrayList<>());

		InitClarify clarify = destiny.getInit().getClarify().stream()
				.filter(item -> fileDocument.getIdGroup().equals(item.getId())).findAny().orElse(null);

		if (clarify == null) {
			clarify = new InitClarify();
			clarify.setId(UUID.randomUUID().toString());
			destiny.getInit().getClarify().add(clarify);
		}

		if (clarify.getResponse() == null)
			clarify.setResponse(new InitClarifyResponse());

		if (clarify.getResponse().getDocument() == null)
			clarify.getResponse().setDocument(new ArrayList<>());

		clarify.getResponse().setAccredited(accredited);
		clarify.getResponse().getDocument().add(documentAproveAccredited);

	}

	private void attachAmendmentRequest(Process destiny, FileBean file) {
		if (destiny.getInit() == null)
			destiny.setInit(new ProcessInit());

		if (destiny.getInit().getAmendment() == null)
			destiny.getInit().setAmendment(new ArrayList<>());

		InitAmendment amendment = destiny.getInit().getAmendment().stream()
				.filter(item -> file.getIdGroup().equals(item.getId())).findAny().orElse(null);

		if (amendment == null) {
			amendment = new InitAmendment();
			amendment.setId(UUID.randomUUID().toString());
			destiny.getInit().getAmendment().add(amendment);
		}

		if (amendment.getRequest() == null) {
			amendment.setRequest(new ArrayList<>());
		}

		amendment.getRequest().add(file);
	}

	private void attachAmendmentResponse(Process destiny, FileBean file, boolean accredited) {
		DocumentApproveAccredited documentAproveAccredited = new DocumentApproveAccredited();
		documentAproveAccredited.setDocument(file);

		if (destiny.getInit() == null)
			destiny.setInit(new ProcessInit());

		if (destiny.getInit().getAmendment() == null)
			destiny.getInit().setAmendment(new ArrayList<>());

		InitAmendment amendment = destiny.getInit().getAmendment().stream()
				.filter(item -> file.getIdGroup().equals(item.getId())).findAny().orElse(null);

		if (amendment == null) {
			amendment = new InitAmendment();
			amendment.setId(UUID.randomUUID().toString());
			destiny.getInit().getAmendment().add(amendment);
		}

		if (amendment.getResponse() == null)
			amendment.setResponse(new InitAmendmentResponse());

		if (amendment.getResponse().getDocument() == null)
			amendment.getResponse().setDocument(new ArrayList<>());

		amendment.getResponse().setAccredited(accredited);
		amendment.getResponse().getDocument().add(documentAproveAccredited);
	}

	private void attachAmendmentNoObjection(Process destiny, FileBean file) {

		if (destiny.getInit() == null)
			destiny.setInit(new ProcessInit());

		if (destiny.getInit().getAmendment() == null)
			destiny.getInit().setAmendment(new ArrayList<>());

		InitAmendment amendment = destiny.getInit().getAmendment().stream()
				.filter(item -> file.getIdGroup().equals(item.getId())).findAny().orElse(null);

		if (amendment == null) {
			amendment = new InitAmendment();
			amendment.setId(UUID.randomUUID().toString());
			destiny.getInit().getAmendment().add(amendment);
		}

		if (amendment.getNoObjection() == null) {
			amendment.setNoObjection(new ArrayList<>());
		}

		amendment.getNoObjection().add(file);
	}

	private void attachAmendmentResponseNoObjection(Process destiny, FileBean file) {

		if (destiny.getInit() == null)
			destiny.setInit(new ProcessInit());

		if (destiny.getInit().getAmendment() == null)
			destiny.getInit().setAmendment(new ArrayList<>());

		InitAmendment amendment = destiny.getInit().getAmendment().stream()
				.filter(item -> file.getIdGroup().equals(item.getId())).findAny().orElse(null);

		if (amendment == null) {
			amendment = new InitAmendment();
			amendment.setId(UUID.randomUUID().toString());
			destiny.getInit().getAmendment().add(amendment);
		}

		if (amendment.getResponseNoObjection() == null) {
			amendment.setResponseNoObjection(new ArrayList<>());
		}

		amendment.getResponseNoObjection().add(file);
	}

	private void attachActOppening(Process destiny, FileBean file) {

		if (destiny.getOpeningAct() == null)
			destiny.setOpeningAct(new OpeningAct());

		if (destiny.getOpeningAct().getDocument() == null)
			destiny.getOpeningAct().setDocument(new ArrayList<>());

		destiny.getOpeningAct().getDocument().add(file);

	}

	private void attachEvaluationReport(Process destiny, FileBean file, boolean accredited) {

		/*ADD 24/04/2020 by rdelaros@everis.com */
		DocumentApproveAccredited documentAproveAccredited = new DocumentApproveAccredited();
		documentAproveAccredited.setDocument(file);
		/**/

		if (destiny.getEvaluationReport() == null)
			destiny.setEvaluationReport(new EvaluationReport());

		if (destiny.getEvaluationReport().getEvaluation() == null)
			destiny.getEvaluationReport().setEvaluation(new EvaluationReportAttach());

		if (destiny.getEvaluationReport().getEvaluation().getDocument() == null)
			destiny.getEvaluationReport().getEvaluation().setDocument(new ArrayList<>());

		destiny.getEvaluationReport().getEvaluation().setAccredited(accredited);

		/*ADD 24/04/2020 by rdelaros@everis.com */
		//destiny.getEvaluationReport().getEvaluation().getDocument().add(file);
		destiny.getEvaluationReport().getEvaluation().getDocument().add(documentAproveAccredited);
		/**/

	}

	private void attachEvaluationReportNoObjection(Process destiny, FileBean file) {
		if (destiny.getEvaluationReport() == null)
			destiny.setEvaluationReport(new EvaluationReport());

		if (destiny.getEvaluationReport().getNoObjection() == null)
			destiny.getEvaluationReport().setNoObjection(new ArrayList<>());

		destiny.getEvaluationReport().getNoObjection().add(file);

	}

	private void attachEvaluationReportResponseNoObjection(Process destiny, FileBean file) {
		if (destiny.getEvaluationReport() == null)
			destiny.setEvaluationReport(new EvaluationReport());

		if (destiny.getEvaluationReport().getResponseNoObjection() == null)
			destiny.getEvaluationReport().setResponseNoObjection(new ArrayList<>());

		destiny.getEvaluationReport().getResponseNoObjection().add(file);

	}

	/* QUIT RDELAROSA 12/06/2020 */
	/*
	private void attachAwardResolution(Process destiny, FileBean file, boolean accredited) {

		if (destiny.getAwardResolution() == null)
			destiny.setAwardResolution(new AwardResolution());

		if (destiny.getAwardResolution().getDocument() == null)
			destiny.getAwardResolution().setDocument(new ArrayList<>());

		destiny.getAwardResolution().setAccredited(accredited);
		destiny.getAwardResolution().getDocument().add(file);
	}
	*/
	
    /* RDELAROSA 12/06/2020 */
	private void attachAwardResolution(Process destiny, FileBean file) {

		if (destiny.getAwardResolution() == null)
			destiny.setAwardResolution(new AwardResolution());

		if (destiny.getAwardResolution().getDocument() == null)
			destiny.getAwardResolution().setDocument(new ArrayList<>());

		destiny.getAwardResolution().getDocument().add(file);
	}
	/*****************************************************************/
	
	private void attachSignedContract(Process destiny, FileBean file, boolean accredited) {

		/*ADD 30/04/2020 by acondori@evers.com */
		DocumentApproveAccredited documentApproveAccredited=new DocumentApproveAccredited();
		documentApproveAccredited.setDocument(file);
		/**/
		if (destiny.getSignedContract() == null)
			destiny.setSignedContract(new ProcessSignedContract());

		if (destiny.getSignedContract().getContract() == null)
			destiny.getSignedContract().setContract(new ArrayList<>());

		Contract contract = destiny.getSignedContract().getContract().stream()
				.filter(item -> file.getIdGroup().equals(item.getId())).findAny().orElse(null);

		if (contract == null) {
			contract = new Contract();
			contract.setId(UUID.randomUUID().toString());
			destiny.getSignedContract().getContract().add(contract);
		}

		if (contract.getDocument() == null)
			contract.setDocument(new ArrayList<>());

		contract.setAccredited(accredited);
		/*ADD 30/04/2020 by acondori@evers.com */
		//contract.getDocument().add(file);
		contract.getDocument().add(documentApproveAccredited);
		/**/
	}

}
