package com.invest.honduras.domain.model.process;

import com.invest.honduras.domain.model.Role;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Committee {
	private String id;
	private String user;
	private Role role;
	private String detailRole;
}
