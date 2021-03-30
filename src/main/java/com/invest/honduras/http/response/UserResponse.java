package com.invest.honduras.http.response;

import java.util.List;

import com.invest.honduras.http.HttpResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse extends HttpResponse<List<UserItemResponse>> {

	public UserResponse(int status, List<UserItemResponse> data) {
		super(status, data);
	}
	
	public UserResponse(int status, String message) {
		super(status, null, message);
	}
}
