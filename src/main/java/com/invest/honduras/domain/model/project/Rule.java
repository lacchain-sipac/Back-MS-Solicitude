package com.invest.honduras.domain.model.project;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "rules")
public class Rule {

	private String code;
	private List<Phase> fases; 	
	
}
