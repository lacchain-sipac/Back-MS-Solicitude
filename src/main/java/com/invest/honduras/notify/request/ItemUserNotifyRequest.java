package com.invest.honduras.notify.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemUserNotifyRequest {
	private String email;
	private String userId;
	private String name;
	private String obs;
	private String template;
	private String subject;
	private String detailsURL;
	
	private String  typeNotify;

}
