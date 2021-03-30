package com.invest.honduras.domain.model.process;

import java.util.List;

import com.invest.honduras.domain.model.FileBean;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class InitAmendment {
	private String id;
	private List<FileBean> request; 
	private InitAmendmentResponse response; 
	private List<FileBean>  noObjection;
	private List<FileBean> responseNoObjection;
}
