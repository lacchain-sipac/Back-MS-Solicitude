package com.invest.honduras.domain.model.process;

import java.util.List;

import com.invest.honduras.domain.model.Approved;
import com.invest.honduras.domain.model.FileBean;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class DocumentApprove {
	private FileBean document;
	private List<Approved> approved;
}
