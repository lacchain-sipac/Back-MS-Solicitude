package com.invest.honduras.domain.model.process;

import org.springframework.http.codec.multipart.FilePart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlowFile {
	private String idGroup;
	private String idProject;
	private String codeStep;
	private String documentType;
	private String observation;
	private boolean accredited;
	private FilePart filePart;
	private String signedContract;
}
