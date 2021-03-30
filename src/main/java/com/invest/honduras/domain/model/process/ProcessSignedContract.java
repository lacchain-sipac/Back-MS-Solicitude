package com.invest.honduras.domain.model.process;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessSignedContract {

	private String numberPrism;

	private boolean accredited;
	private List<Contract> contract;
}
