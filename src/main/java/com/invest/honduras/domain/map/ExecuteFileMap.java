package com.invest.honduras.domain.map;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.invest.honduras.domain.model.FileBean;
import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.domain.model.User;
import com.invest.honduras.domain.model.execute.Advance;
import com.invest.honduras.domain.model.execute.AdvanceRequest;
import com.invest.honduras.domain.model.execute.Execute;
import com.invest.honduras.domain.model.execute.FinalPayment;
import com.invest.honduras.domain.model.execute.FinalPaymentRequest;
import com.invest.honduras.domain.model.execute.ModifiedContract;
import com.invest.honduras.domain.model.execute.PaymentEstimate;
import com.invest.honduras.domain.model.execute.PaymentEstimateRequest;
import com.invest.honduras.domain.model.execute.PaymentManagement;
import com.invest.honduras.domain.model.process.DocumentApproveAccredited;
import com.invest.honduras.domain.model.process.FlowFile;
import com.invest.honduras.util.FlowConstants;

public class ExecuteFileMap {

	public void mappingFileInsert(Execute destiny, FlowFile flowFile, Storage source, User user) {

		FileBean fileBean = new FileBean();
		fileBean.setIdStorage(source.getId());
		fileBean.setDateCreation(new Date());
		fileBean.setFileName(source.getFileName());
		fileBean.setObservation(source.getObservation());
		fileBean.setUser(String.format("%s %s", user.getFullname(), user.getSurnames()));

		fileBean.setHash(source.getHash());
		fileBean.setIdGroup(flowFile.getIdGroup() == null ? "" : flowFile.getIdGroup());
		fileBean.setSignedContract(flowFile.getSignedContract());
		destiny.setLastModifiedBy(user.getEmail());
		destiny.setLastModifiedDate(new Date());

		String documentType = flowFile.getDocumentType();
		boolean accredited = flowFile.isAccredited();
		String company = user.getCompany();

		switch (flowFile.getCodeStep()) {

		case FlowConstants.STEP_PAYMENT:

			if (FlowConstants.DOC_ADVANCE_REQUEST.equals(documentType))
				attachAdvanceRequest(destiny, fileBean, accredited, company);

			if (FlowConstants.DOC_ADVANCE_F01.equals(documentType))
				attachAdvanceF01(destiny, fileBean);

			if (FlowConstants.DOC_ADVANCE_PROOF_PAYMENT.equals(documentType))
				attachAdvanceProffPayment(destiny, fileBean);

			if (FlowConstants.DOC_ESTIMATE_REQUEST.equals(documentType))
				attachEstimatedPaymentRequest(destiny, fileBean, accredited, company);

			if (FlowConstants.DOC_ESTIMATE_F01.equals(documentType))
				attachEstimatedPaymentF01(destiny, fileBean);

			if (FlowConstants.DOC_ESTIMATE_PROOF_PAYMENT.equals(documentType))
				attachEstimatedPaymentProffPayment(destiny, fileBean);

			if (FlowConstants.DOC_FINAL_PAYMENT_REQUEST.equals(documentType))
				attachFinalPaymentRequest(destiny, fileBean, accredited, company);

			if (FlowConstants.DOC_FINAL_PAYMENT_F01.equals(documentType))
				attachFinalPaymentF01(destiny, fileBean);

			if (FlowConstants.DOC_FINAL_PAYMENT_PROOF_PAYMENT.equals(documentType))
				attachFinalPaymentProffPayment(destiny, fileBean);

			if (FlowConstants.DOC_CONTRACT_MODIFICATE_INIT.equals(documentType))
				attachModifiedContractRequestInit(destiny, fileBean, accredited, company);

			break;

		case FlowConstants.STEP_CONTRACT_MODIFICATE:
			if (FlowConstants.DOC_CONTRACT_MODIFICATE.equals(documentType))
				attachModifiedContractRequest(destiny, fileBean, accredited, company);

			break;

		default:
			break;
		}

	}

	private void attachAdvanceRequest(Execute destiny, FileBean fileDocument, boolean accredited, String company) {

		DocumentApproveAccredited documentAproveAccredited = new DocumentApproveAccredited();
		fileDocument.setCompany(company);
		documentAproveAccredited.setDocument(fileDocument);

		if (destiny.getPayment() == null)
			destiny.setPayment(new PaymentManagement());

		if (destiny.getPayment().getAdvance() == null)
			destiny.getPayment().setAdvance(new ArrayList<>());

		Advance advance = destiny.getPayment().getAdvance().stream()
				.filter(item -> fileDocument.getIdGroup().equals(item.getId())).findAny().orElse(null);

		if (advance == null) {
			advance = new Advance();
			advance.setId(UUID.randomUUID().toString());
			destiny.getPayment().getAdvance().add(advance);
		}

		if (advance.getRequest() == null)
			advance.setRequest(new AdvanceRequest());

		if (advance.getRequest().getDocument() == null)
			advance.getRequest().setDocument(new ArrayList<>());

		advance.getRequest().setAccredited(accredited);
		advance.getRequest().getDocument().add(documentAproveAccredited);

	}

	private void attachAdvanceF01(Execute destiny, FileBean fileDocument) {

		if (destiny.getPayment() == null)
			destiny.setPayment(new PaymentManagement());

		if (destiny.getPayment().getAdvance() == null)
			destiny.getPayment().setAdvance(new ArrayList<>());

		Advance advance = destiny.getPayment().getAdvance().stream()
				.filter(item -> fileDocument.getIdGroup().equals(item.getId())).findAny().orElse(null);

		if (advance == null) {
			advance = new Advance();
			advance.setId(UUID.randomUUID().toString());
			destiny.getPayment().getAdvance().add(advance);
		}

		if (advance.getF01() == null)
			advance.setF01(new ArrayList<>());

		advance.getF01().add(fileDocument);

	}

	private void attachAdvanceProffPayment(Execute destiny, FileBean fileDocument) {

		if (destiny.getPayment() == null)
			destiny.setPayment(new PaymentManagement());

		if (destiny.getPayment().getAdvance() == null)
			destiny.getPayment().setAdvance(new ArrayList<>());

		Advance advance = destiny.getPayment().getAdvance().stream()
				.filter(item -> fileDocument.getIdGroup().equals(item.getId())).findAny().orElse(null);

		if (advance == null) {
			advance = new Advance();
			advance.setId(UUID.randomUUID().toString());
			destiny.getPayment().getAdvance().add(advance);
		}

		if (advance.getProffPayment() == null)
			advance.setProffPayment(new ArrayList<>());

		advance.getProffPayment().add(fileDocument);

	}

	private void attachEstimatedPaymentRequest(Execute destiny, FileBean fileDocument, boolean accredited,
			String company) {
		fileDocument.setCompany(company);
		DocumentApproveAccredited documentAproveAccredited = new DocumentApproveAccredited();
		documentAproveAccredited.setDocument(fileDocument);

		if (destiny.getPayment() == null)
			destiny.setPayment(new PaymentManagement());

		if (destiny.getPayment().getEstimate() == null)
			destiny.getPayment().setEstimate(new ArrayList<>());

		PaymentEstimate estimate = destiny.getPayment().getEstimate().stream()
				.filter(item -> fileDocument.getIdGroup().equals(item.getId())).findAny().orElse(null);

		if (estimate == null) {
			estimate = new PaymentEstimate();
			estimate.setId(UUID.randomUUID().toString());
			destiny.getPayment().getEstimate().add(estimate);
		}

		if (estimate.getRequest() == null)
			estimate.setRequest(new PaymentEstimateRequest());

		if (estimate.getRequest().getDocument() == null)
			estimate.getRequest().setDocument(new ArrayList<>());

		estimate.getRequest().setAccredited(accredited);
		estimate.getRequest().getDocument().add(documentAproveAccredited);

	}

	private void attachEstimatedPaymentF01(Execute destiny, FileBean fileDocument) {

		if (destiny.getPayment() == null)
			destiny.setPayment(new PaymentManagement());

		if (destiny.getPayment().getEstimate() == null)
			destiny.getPayment().setEstimate(new ArrayList<>());

		PaymentEstimate estimate = destiny.getPayment().getEstimate().stream()
				.filter(item -> fileDocument.getIdGroup().equals(item.getId())).findAny().orElse(null);

		if (estimate == null) {
			estimate = new PaymentEstimate();
			estimate.setId(UUID.randomUUID().toString());
			destiny.getPayment().getEstimate().add(estimate);
		}

		if (estimate.getF01() == null)
			estimate.setF01(new ArrayList<>());

		estimate.getF01().add(fileDocument);

	}

	private void attachEstimatedPaymentProffPayment(Execute destiny, FileBean fileDocument) {

		if (destiny.getPayment() == null)
			destiny.setPayment(new PaymentManagement());

		if (destiny.getPayment().getEstimate() == null)
			destiny.getPayment().setEstimate(new ArrayList<>());

		PaymentEstimate estimate = destiny.getPayment().getEstimate().stream()
				.filter(item -> fileDocument.getIdGroup().equals(item.getId())).findAny().orElse(null);

		if (estimate == null)
			estimate = new PaymentEstimate();
		
		if (estimate.getProffPayment() == null)
			estimate.setProffPayment(new ArrayList<>());

		estimate.getProffPayment().add(fileDocument);

	}

	private void attachFinalPaymentRequest(Execute destiny, FileBean fileDocument, boolean accredited, String company) {
		fileDocument.setCompany(company);
		DocumentApproveAccredited documentAproveAccredited = new DocumentApproveAccredited();
		documentAproveAccredited.setDocument(fileDocument);

		if (destiny.getPayment() == null)
			destiny.setPayment(new PaymentManagement());

		if (destiny.getPayment().getFinalPayment() == null)
			destiny.getPayment().setFinalPayment(new ArrayList<>());

		FinalPayment paymentFinal = destiny.getPayment().getFinalPayment().stream()
				.filter(item -> fileDocument.getIdGroup().equals(item.getId())).findAny().orElse(null);

		if (paymentFinal == null) {
			paymentFinal = new FinalPayment();
			paymentFinal.setId(UUID.randomUUID().toString());
			destiny.getPayment().getFinalPayment().add(paymentFinal);
		}

		if (paymentFinal.getRequest() == null)
			paymentFinal.setRequest(new FinalPaymentRequest());

		if (paymentFinal.getRequest().getDocument() == null)
			paymentFinal.getRequest().setDocument(new ArrayList<>());

		paymentFinal.getRequest().setAccredited(accredited);
		paymentFinal.getRequest().getDocument().add(documentAproveAccredited);

	}

	private void attachFinalPaymentF01(Execute destiny, FileBean fileDocument) {

		if (destiny.getPayment() == null)
			destiny.setPayment(new PaymentManagement());

		if (destiny.getPayment().getFinalPayment() == null)
			destiny.getPayment().setFinalPayment(new ArrayList<>());

		FinalPayment finalPayment = destiny.getPayment().getFinalPayment().stream()
				.filter(item -> fileDocument.getIdGroup().equals(item.getId())).findAny().orElse(null);

		if (finalPayment == null) {
			finalPayment = new FinalPayment();
			finalPayment.setId(UUID.randomUUID().toString());
			destiny.getPayment().getFinalPayment().add(finalPayment);
		}

		if (finalPayment.getF01() == null)
			finalPayment.setF01(new ArrayList<>());

		finalPayment.getF01().add(fileDocument);

	}

	private void attachFinalPaymentProffPayment(Execute destiny, FileBean fileDocument) {

		if (destiny.getPayment() == null)
			destiny.setPayment(new PaymentManagement());

		if (destiny.getPayment().getFinalPayment() == null)
			destiny.getPayment().setFinalPayment(new ArrayList<>());

		FinalPayment finalPayment = destiny.getPayment().getFinalPayment().stream()
				.filter(item -> fileDocument.getIdGroup().equals(item.getId())).findAny().orElse(null);

		if (finalPayment == null) {
			finalPayment = new FinalPayment();
		}
		
		if (finalPayment.getProffPayment() == null)
			finalPayment.setProffPayment(new ArrayList<>());

		finalPayment.getProffPayment().add(fileDocument);

	}

	private void attachModifiedContractRequestInit(Execute destiny, FileBean fileDocument, boolean accredited,
			String company) {

		if (destiny.getPayment() == null)
			destiny.setPayment(new PaymentManagement());

		if (destiny.getPayment().getModifiedContract() == null)
			destiny.getPayment().setModifiedContract(new ModifiedContract());

		if (destiny.getPayment().getModifiedContract().getDocument() == null)
			destiny.getPayment().getModifiedContract().setDocument(new ArrayList<>());

		fileDocument.setCompany(company);
		DocumentApproveAccredited documentAproveAccredited = new DocumentApproveAccredited();
		documentAproveAccredited.setDocument(fileDocument);

		destiny.getPayment().getModifiedContract().setAccredited(accredited);
		destiny.getPayment().getModifiedContract().getDocument().add(documentAproveAccredited);
	}

	private void attachModifiedContractRequest(Execute destiny, FileBean fileDocument, boolean accredited,
			String company) {
		fileDocument.setCompany(company);
		DocumentApproveAccredited documentAproveAccredited = new DocumentApproveAccredited();
		documentAproveAccredited.setDocument(fileDocument);

		if (destiny.getModifiedContract() == null)
			destiny.setModifiedContract(new ModifiedContract());

		if (destiny.getModifiedContract().getDocument() == null)
			destiny.getModifiedContract().setDocument(new ArrayList<>());

		destiny.getModifiedContract().setAccredited(accredited);
		destiny.getModifiedContract().getDocument().add(documentAproveAccredited);

	}
}
