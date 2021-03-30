package com.invest.honduras.domain.model.process;

import java.util.List;

import com.invest.honduras.domain.model.FileBean;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Contract {
	private String id ;
	private boolean accredited;
	/*ADD 30/04/2020 by acondori@evers.com */
	//private List<FileBean> document;
	private List<DocumentApproveAccredited> document;
	/**/
}
