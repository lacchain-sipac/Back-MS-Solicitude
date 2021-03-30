package com.invest.honduras.domain.model.solicitude;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "parameter")
public class Parameter {
	
	private String typeParameter;
	
	private List<Object> parameters;

}
