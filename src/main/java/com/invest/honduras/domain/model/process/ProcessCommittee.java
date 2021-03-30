package com.invest.honduras.domain.model.process;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProcessCommittee {

	private List<Committee> committee;

	private boolean accredited;
}
