package com.invest.honduras.http.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StepRequest {
	private String projectCode;
	private String proxyAddress;
	private String role;
	private String step;
}
