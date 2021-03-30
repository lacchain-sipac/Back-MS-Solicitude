package com.invest.honduras.http.response;

import java.util.List;

import com.invest.honduras.http.HttpResponse;

public class ProjectAllResponse extends HttpResponse<List<ProjectItemResponse>> {

	public ProjectAllResponse(int status, List<ProjectItemResponse> data) {
		super(status, data);
	}
	
	public ProjectAllResponse(int status, String message) {
		super(status, null, message);
	}

}