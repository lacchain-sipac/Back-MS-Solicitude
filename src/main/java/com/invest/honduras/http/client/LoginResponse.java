package com.invest.honduras.http.client;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class LoginResponse  {	
	
	private String status;
	
	@JsonProperty("data")
    private  DataResponse dataResponse;
}
