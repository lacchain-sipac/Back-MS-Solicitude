package com.invest.honduras.http.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseGeneralDocument {

	 private int status;
	 private DocumentItem data;
	 private String error;

}