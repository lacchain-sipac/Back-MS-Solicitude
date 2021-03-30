package com.invest.honduras.http.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseGeneralProject {

	 private int status;
	 private ProjectItem data;
	 private String error;

}