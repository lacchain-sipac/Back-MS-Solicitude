package com.invest.honduras.http.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentItem {
	private String hash;
	private String user;
	private String role;
	private boolean isfinal;
	private long creation;


}
