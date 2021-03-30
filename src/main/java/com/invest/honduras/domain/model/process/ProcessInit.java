package com.invest.honduras.domain.model.process;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessInit {

	private InitPreparation preparation;

	private List<InitClarify> clarify;
	
	private List<InitAmendment> amendment;
	
	private boolean accredited;
}
