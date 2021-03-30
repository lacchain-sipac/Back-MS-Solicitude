package com.invest.honduras.domain.model.execute;
import java.util.List;

import com.invest.honduras.domain.model.process.DocumentApproveAccredited;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentEstimateRequest {
	private List<DocumentApproveAccredited> document;
	private boolean accredited;
}
