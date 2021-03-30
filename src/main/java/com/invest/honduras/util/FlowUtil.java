package com.invest.honduras.util;

import com.invest.honduras.domain.model.project.DocumentType;
import com.invest.honduras.domain.model.project.DocumentTypeAssociated;
import com.invest.honduras.domain.model.project.Phase;
import com.invest.honduras.domain.model.project.Rule;
import com.invest.honduras.domain.model.project.Step;

public class FlowUtil {

	public static String getDescriptionByCodeDocument(Rule rule, String codeDocument) {
		 
		for (Phase phase : rule.getFases()) {

			for (Step step : phase.getStep()) {

				for (DocumentType doc : step.getDocumentType()) {
					if (codeDocument.equals(doc.getCode())) {
						return doc.getDescription();
					}
					if (doc.getDocumentTypeAssociated() != null)
						for (DocumentTypeAssociated type : doc.getDocumentTypeAssociated()) {
							if (codeDocument.equals(type.getCode())) {
								return type.getDescription();
							}
						}
				}
			}
		}
		return null;
	}

}
