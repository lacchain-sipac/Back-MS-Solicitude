package com.invest.honduras.http.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseGeneralBool {

	 private int status;
	 private ResponseBoolItem data;
	 private String error;

}