package com.invest.honduras.domain.model.vc;

import java.util.Date;

import com.everis.blockchain.honduras.util.HashUtils;
import com.everis.blockchain.honduras.util.MnidUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.invest.honduras.domain.model.Accredited;
import com.invest.honduras.domain.model.Configuration;
import com.invest.honduras.domain.model.User;
import com.invest.honduras.domain.model.solicitude.Solicitude;
import com.invest.honduras.vc.ConstansVC;
import com.invest.honduras.vc.Proof;

public class CredentialSubjectMap {

	public void calculateHash(Solicitude source, Configuration app) {

	
		CredentialSubject subject = new CredentialSubject(source);

		VerifiableCredentialProject vc = new VerifiableCredentialProject();
		vc.setCredentialSubject(subject);
		vc.setIssuanceDate(source.getAccredited().getDate());
		vc.setIssuer(MnidUtils.DID + MnidUtils.encode(app.getNetworkId(), source.getAccredited().getProxy()));

		Proof proof = new Proof();
		proof.setType(ConstansVC.PROOF_TYPE);
		proof.setContractAddress(app.getContractAddressStepManager());// VR
		proof.setNetworkId(app.getNetworkId());

		vc.setProof(proof);

		Gson gson = new GsonBuilder().setDateFormat(ConstansVC.VERIFICABLE_CREDENTIAL_FORMAT_DATE_ISO).create();

		System.out.println(gson.toJson(vc));
		
		source.getAccredited().setHash(HashUtils.convetStringToHashKeccakHex(gson.toJson(vc)));
		
		

	}

	public void mappingAccredited(Solicitude source, String role, User user,  String observation) {
		Accredited accredited = new Accredited();
		accredited.setUser(String.format("%s %s", user.getFullname(), user.getSurnames()));
		accredited.setRole(role);
		accredited.setObservation(observation);
		accredited.setDate(new Date());
		accredited.setProxy(user.getProxyAddress());
		source.setAccredited(accredited);
		
		source.setLastModifiedBy(user.getEmail());
		source.setLastModifiedDate(new Date());

	}

}
