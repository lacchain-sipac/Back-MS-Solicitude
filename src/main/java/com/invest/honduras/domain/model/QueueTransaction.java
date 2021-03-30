	package com.invest.honduras.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "queue_tx")
public class QueueTransaction {
	@Id
	private String id;
	private UserRoleProjectQueue userRoleProject;
	
	private int attempts;
}
