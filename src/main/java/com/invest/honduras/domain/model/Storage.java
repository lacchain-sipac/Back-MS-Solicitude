package com.invest.honduras.domain.model;

import org.springframework.core.io.Resource;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "project_document")
public class Storage  {
	@Id
	private String id;
	private String fileName;
	private String contentType;
    private String observation;
	private String hash;
	private Resource file;
	//private byte[] data;

}
