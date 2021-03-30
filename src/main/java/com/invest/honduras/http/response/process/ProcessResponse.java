package com.invest.honduras.http.response.process;

import com.invest.honduras.http.HttpResponse;

public class ProcessResponse extends HttpResponse<ProcessItemResponse> {

	
	public ProcessResponse(int status, ProcessItemResponse data ) {
		super(status, data);
	}
	
	public ProcessResponse(int status ,String  message) {
		super(status, null , message);
	}

}
