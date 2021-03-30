package com.invest.honduras.domain.map;

import java.util.ArrayList;
import java.util.Date;

import com.invest.honduras.domain.model.Approved;
import com.invest.honduras.domain.model.User;
import com.invest.honduras.domain.model.execute.Execute;
import com.invest.honduras.domain.model.process.DocumentApproveAccredited;
import com.invest.honduras.http.client.process.ApproveRequest;
import com.invest.honduras.util.FlowConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExecuteApprovedMap {

	public void mapping(ApproveRequest request, String hash, Execute destiny, User user, boolean accredited) {

		String idStorage = request.getIdStorage();

		Approved approved = new Approved();
		approved.setHash(hash);
		
		/*RDELAROSA ADD ROLE 17/06/2020 */
		log.info("EXECUTE - USER - ROL :"+request.getRole());
		log.info("EXECUTE - USER - ROLNAME :"+request.getRoleName());
		
		approved.setUser(String.format("%s %s", user.getFullname(), user.getSurnames())+" - " + request.getRoleName());

		approved.setObservation(request.getObservation());
		approved.setDate(new Date());
		approved.setType(request.getType());

		destiny.setLastModifiedBy(user.getEmail());
		destiny.setLastModifiedDate(new Date());

		switch (request.getCodeStep()) {
		case FlowConstants.STEP_PAYMENT:

			if (FlowConstants.DOC_ADVANCE_REQUEST.equals(request.getDocumentType()))
				attachAdvanceRequest(idStorage, destiny, approved, request, accredited);

			if (FlowConstants.DOC_ESTIMATE_REQUEST.equals(request.getDocumentType()))
				attachEstimatedPaymentRequest(idStorage, destiny, approved, request, accredited);

			if (FlowConstants.DOC_FINAL_PAYMENT_REQUEST.equals(request.getDocumentType()))
				attachFinalPaymentRequest(idStorage, destiny, approved, request, accredited);

			if (FlowConstants.DOC_CONTRACT_MODIFICATE_INIT.equals(request.getDocumentType()))
				attachModifiedContractRequestInit(idStorage, destiny, approved, request, accredited);
			break;

		case FlowConstants.STEP_CONTRACT_MODIFICATE:
			if (FlowConstants.DOC_CONTRACT_MODIFICATE.equals(request.getDocumentType()))
				attachModifiedContractRequest(idStorage, destiny, approved, request, accredited);

		}

	}

	private void attachAdvanceRequest(String idStorage, Execute destiny, Approved appoved, ApproveRequest request,
			boolean accredited) {

		if (destiny.getPayment() != null && destiny.getPayment().getAdvance() != null)

			destiny.getPayment().getAdvance().forEach(item -> { // .getRequest().getDocument()) {

				if (item.getRequest() != null && item.getRequest().getDocument() != null)

					item.getRequest().getDocument().forEach(document -> {
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

	private void attachEstimatedPaymentRequest(String idStorage, Execute destiny, Approved appoved,
			ApproveRequest request, boolean accredited) {

		if (destiny.getPayment() != null && destiny.getPayment().getEstimate() != null)

			destiny.getPayment().getEstimate().forEach(item -> {

				if (item.getRequest() != null && item.getRequest().getDocument() != null)
					item.getRequest().getDocument().forEach(document -> {
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

	private void attachFinalPaymentRequest(String idStorage, Execute destiny, Approved appoved, ApproveRequest request,
			boolean accredited) {
		if (destiny.getPayment() != null && destiny.getPayment().getFinalPayment() != null)

			destiny.getPayment().getFinalPayment().forEach(item -> {

				if (item.getRequest() != null && item.getRequest().getDocument() != null)
					item.getRequest().getDocument().forEach(document -> {
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

	private void attachModifiedContractRequestInit(String idStorage, Execute destiny, Approved appoved,
			ApproveRequest request, boolean accredited) {
		if (destiny.getPayment() != null && destiny.getPayment().getModifiedContract() != null
				&& destiny.getPayment().getModifiedContract().getDocument() != null)

			destiny.getPayment().getModifiedContract().getDocument().forEach(item -> {

				if (item.getDocument() != null)

					if (idStorage.equals(item.getDocument().getIdStorage())) {

						request.setNameFile(item.getDocument().getFileName());
						if (accredited) {
							item.setApproved(appoved);
						} else {
							if (item.getReview() == null)
								item.setReview(new ArrayList<>());
							item.getReview().add(appoved);
						}
					}
			});

	}

	private void attachModifiedContractRequest(String idStorage, Execute destiny, Approved appoved,
			ApproveRequest request, boolean accredited) {
		if (destiny.getModifiedContract() != null && destiny.getModifiedContract().getDocument() != null)
			for (DocumentApproveAccredited document : destiny.getModifiedContract().getDocument()) {

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

}
