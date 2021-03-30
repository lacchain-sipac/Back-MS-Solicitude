package com.invest.honduras.domain.model.process;

import java.util.List;

import com.invest.honduras.domain.model.FileBean;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AwardResolution {
	private boolean accredited;
	private List<FileBean> document;
}
