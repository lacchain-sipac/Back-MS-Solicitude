package com.invest.honduras.http.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleProjectRequest {
	private String role;
	private String user;
	private String projectCodeHash;
	private String proxyAddressUserSession;
}
