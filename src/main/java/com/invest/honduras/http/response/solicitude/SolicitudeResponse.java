package com.invest.honduras.http.response.solicitude;

import com.invest.honduras.http.HttpResponse;

public class SolicitudeResponse extends HttpResponse<SolicitudeItemResponse> {

	
	public SolicitudeResponse(int status, SolicitudeItemResponse data ) {
		super(status, data );
	}
	public SolicitudeResponse(int status,String  message) {
		super(status, null , message);
	}
}
