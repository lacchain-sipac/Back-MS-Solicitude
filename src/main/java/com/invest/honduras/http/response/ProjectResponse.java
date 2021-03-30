package com.invest.honduras.http.response;

import com.invest.honduras.http.HttpResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectResponse extends HttpResponse<ProjectItemResponse> {

	public ProjectResponse(int status,  ProjectItemResponse data) {
		super(status, data);
	}
	
	public ProjectResponse(int status,  String message) {
		super(status, null, message);
	}
}
