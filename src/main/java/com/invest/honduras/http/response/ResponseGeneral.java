package com.invest.honduras.http.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseGeneral {

	 private int status;
	 private ResponseItem data;
	 private String error;

}