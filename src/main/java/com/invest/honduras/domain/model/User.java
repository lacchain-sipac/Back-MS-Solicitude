package com.invest.honduras.domain.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.invest.honduras.http.client.RoleRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "user")
public class User {
	private String id;
	private String surnames;
	private String fullname;
	private String email;
	private String company;
	private List<RoleRequest> roles;
	private String password;
	private String codeStatus;
	private String proxyAddress;

}
