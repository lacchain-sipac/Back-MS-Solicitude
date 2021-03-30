package com.invest.honduras.http.client;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class UserToNotifyRequest {

	private String email;
	private String userId;
	private String name;
	private String state;
	private String obs;
	private String typeNotify;
	private String modifyBy;
	private List<String> roles;

}