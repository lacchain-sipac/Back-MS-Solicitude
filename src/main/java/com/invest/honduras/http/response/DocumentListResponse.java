package com.invest.honduras.http.response;

import java.util.List;

import com.invest.honduras.http.HttpResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentListResponse extends HttpResponse<List<DocumentResponse>> {

	public DocumentListResponse(int status, List<DocumentResponse> data) {		
		super(status, data);
	}

	public DocumentListResponse(int status, String message) {		
		super(status, null, message);
	}
}
