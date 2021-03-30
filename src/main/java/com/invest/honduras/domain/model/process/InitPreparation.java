package com.invest.honduras.domain.model.process;

import java.util.List;

import com.invest.honduras.domain.model.FileBean; 

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitPreparation {

	private String linkRepository;
	private InitPreparationDocument bidding;
	private List<FileBean> noObjection;
	private List<FileBean> responseNoObjection;

}
