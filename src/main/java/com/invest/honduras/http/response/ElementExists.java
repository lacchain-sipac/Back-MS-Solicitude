package com.invest.honduras.http.response;

import com.invest.honduras.http.HttpResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElementExists extends HttpResponse<Boolean> {

	public ElementExists(int status,  Boolean data) {
		super(status, data);
	}
	
	public ElementExists(int status,  String error) {
		super(status, null, error);
	}
}
