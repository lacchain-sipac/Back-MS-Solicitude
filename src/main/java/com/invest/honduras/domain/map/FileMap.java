package com.invest.honduras.domain.map;

import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.domain.model.process.FlowFile;

public class FileMap {

	public void mapping( FlowFile flow, Storage storage) {
		
		//storage.setData(bytes);
		storage.setObservation(flow.getObservation());
		//storage.setHash(HashUtils.convetStringToHashKeccakHex(bytes));
		storage.setContentType(flow.getFilePart().headers().getContentType().toString());
		storage.setFileName(flow.getFilePart().filename().replace(" ", "_").trim());

	}
}
