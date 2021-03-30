package com.invest.honduras.domain.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FinancialClosure{
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date date;
	//private String hash;
	private String user;
}
