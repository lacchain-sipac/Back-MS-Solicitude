package com.invest.honduras.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleProjectQueue {
	private String id;
	private String role;
	private String user;
	private String projectCodeHash;
	private String proxyAddressUserSession;
}
