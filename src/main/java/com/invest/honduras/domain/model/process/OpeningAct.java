package com.invest.honduras.domain.model.process;

import java.util.List;

import com.invest.honduras.domain.model.FileBean;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class OpeningAct {

	private List<FileBean> document;

	private boolean accredited;
}
