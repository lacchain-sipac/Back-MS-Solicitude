package com.invest.honduras.domain.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "configuration")
public class Configuration {
	private String id;
	private String blockchainServer;
	//private String privateKeyBackend;
	private String contractAddressIdentityManager;
	private String contractAddressRoles;
	private String networkId;
	private String contractAddressStepManager;
	private String didBackend;
	private String contractAddressFlow;
	private String urlLogin;
}
