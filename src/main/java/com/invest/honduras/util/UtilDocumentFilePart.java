package com.invest.honduras.util;

import java.util.List;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;

import com.invest.honduras.domain.model.process.FlowFile;

public class UtilDocumentFilePart {

	public static FlowFile getParaneter(List<Part> parts) {
		FlowFile flowFile = new FlowFile();
		for (Part part : parts) {
			if (part instanceof FilePart) {
				flowFile.setFilePart( (FilePart) part);
				continue;
			}
			if (part instanceof FormFieldPart) {
				FormFieldPart frmObs = (FormFieldPart) part;
				if (Constant.FILE_STEP_CODE.equals(frmObs.name())) {
					flowFile.setCodeStep(frmObs.value());
				}

				if (Constant.FILE_ID_PROJECT.equals(frmObs.name())) {
					flowFile.setIdProject(frmObs.value());
				}

				if (Constant.FILE_DOCUMENT_TYPE.equals(frmObs.name())) {
					flowFile.setDocumentType(frmObs.value());
				}

				if (Constant.FILE_DOCUMENT_ACCREDITED.equals(frmObs.name())) {
					flowFile.setAccredited("1".equals(frmObs.value()));
				}

				if (Constant.FILE_DOCUMENT_OBSERVATION.equals(frmObs.name())) {
					flowFile.setObservation(frmObs.value());
				}
				
				if (Constant.FILE_DOCUMENT_ID_GROUP.equals(frmObs.name())) {
					flowFile.setIdGroup(frmObs.value());
				}

				if (Constant.FILE_DOCUMENT_SIGNED_CONTRACT.equals(frmObs.name())) {
					flowFile.setSignedContract(frmObs.value());
				}
			}

		}

		return flowFile;
	}

}
