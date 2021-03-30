package com.invest.honduras.domain.model.process;

import java.util.List;

import com.invest.honduras.domain.model.FileBean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InitClarify {
	private String id;
	private List<FileBean> request;
	private InitClarifyResponse response;
}
