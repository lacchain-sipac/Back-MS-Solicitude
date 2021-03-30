package com.invest.honduras.http.client.process;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommitteeDeleteRequest {
	private String idCommittee;
	private String idProcess;
}
