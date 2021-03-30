package com.invest.honduras.http.response.execution;

import com.invest.honduras.http.HttpResponse;

public class ExecuteResponse extends HttpResponse<ExecuteItemResponse> {

	public ExecuteResponse(int status, ExecuteItemResponse data) {
		super(status, data );
	}
	
	public ExecuteResponse(int status, String  error) {
		super(status, null , error);
	}
}
