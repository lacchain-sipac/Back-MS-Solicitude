package com.invest.honduras.http.client.process;

import com.invest.honduras.domain.model.Role;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommitteeRequest {
	
	private String user;
	private Role role;
	private String detailRole;
}
