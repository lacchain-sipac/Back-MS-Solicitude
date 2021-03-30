package com.invest.honduras.domain.model.project;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Phase {

	private String code;
	private List<Step> step;
}
