package com.invest.honduras.http.client;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class NotifyRequest {

	private String typeNotification;
	private List<UserToNotifyRequest> user;
	
}
