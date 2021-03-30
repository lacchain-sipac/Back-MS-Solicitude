package com.invest.honduras.domain.map;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.util.Strings;

import com.invest.honduras.domain.model.FinancialClosure;
import com.invest.honduras.domain.model.Project;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.process.Process;
import com.invest.honduras.domain.model.solicitude.Solicitude;
import com.invest.honduras.domain.model.solicitude.StatusSolicitude;
import com.invest.honduras.http.response.ProjectItemResponse;
import com.invest.honduras.http.response.execution.ExecuteItemResponse;
import com.invest.honduras.http.response.process.ProcessItemResponse;
import com.invest.honduras.http.response.solicitude.SolicitudeItemResponse;
import com.invest.honduras.util.Constant;

public class ProjectMap {

	public void mapping(Project source, ProjectItemResponse destiny, List<StatusSolicitude> statusSolicitudes) {
		destiny.setId(source.getId());

		SolicitudeItemResponse solicitudeResponse = new SolicitudeItemResponse();
		new SolicitudeMap().mapping(source.getSolicitude(), solicitudeResponse);
		solicitudeResponse.setIdProject(source.getId());
		destiny.setSolicitude(solicitudeResponse);

		ProcessItemResponse processResponse = new ProcessItemResponse();
		new ProcessMap().mapping(source.getProcess(), processResponse);
		processResponse.setIdProject(source.getId());
		destiny.setProcess(processResponse);

		ExecuteItemResponse executionResponse = new ExecuteItemResponse();
		new ExecuteMap().mapping(source.getExecute(), executionResponse);
		executionResponse.setIdProject(source.getId());
		destiny.setExecution(executionResponse);

		destiny.setLastModifiedDate(source.getLastModifiedDate());
		destiny.setCreatedBy(source.getCreatedBy());
		destiny.setCreatedDate(source.getCreatedDate());
		destiny.setLastModifiedBy(source.getLastModifiedBy());

		for (StatusSolicitude statusSolicitude : statusSolicitudes) {
			if (source.getCurrentStatus() != null && source.getCurrentStatus().equals(statusSolicitude.getCode())) {
				destiny.setCurrentStatus(statusSolicitude);
				break;
			}
		}
	}

	public void mappingCreate(Solicitude solicitude, Project destiny, UserSession user) {

		destiny.setSolicitude(solicitude);
		destiny.setCurrentStatus(Constant.TypeStatus.INICIADO.getType());

		destiny.setLastModifiedDate(new Date());
		destiny.setCreatedDate(new Date());

		destiny.setCreatedBy(user.getEmail());
		destiny.setLastModifiedBy(user.getEmail());

	}
	
	public void mappingFinish( String id, Project destiny, UserSession user) {

		destiny.setId(id);
		FinancialClosure pt = new FinancialClosure();
		pt.setUser(user.getEmail());
		pt.setDate(new Date());

		destiny.setFinancialClosure(pt);
		
		destiny.setCurrentStatus(Constant.TypeStatus.TERMINADO.getType());

		destiny.setLastModifiedDate(new Date());
		destiny.setCreatedDate(new Date());

		destiny.setCreatedBy(user.getEmail());
		destiny.setLastModifiedBy(user.getEmail());

	}

	public void mappingUpdate(Project source, Project destiny, UserSession user) {

		if (source.getSolicitude() != null) {
			destiny.setSolicitude(source.getSolicitude());
		}

		if (source.getProcess() != null) {
			destiny.setProcess(source.getProcess());
		}

		if (source.getExecute() != null) {
			destiny.setExecute(source.getExecute());
		}
		
		if (source.getFinancialClosure() != null) {
			destiny.setFinancialClosure(source.getFinancialClosure());
		}

		if (!Strings.isEmpty(source.getCurrentStatus())) {
			destiny.setCurrentStatus(source.getCurrentStatus());
		}
		if (!Strings.isEmpty(source.getIdAccredited())) {
			destiny.setIdAccredited(source.getIdAccredited());
		}
		

		destiny.setLastModifiedDate(new Date());

		destiny.setLastModifiedBy(user.getEmail());

	}

	public void finishSolicitude(Project project, Solicitude solicitude) {
		project.setSolicitude(solicitude);
		if (solicitude.isFinishSolicitude()) {
			project.setCurrentStatus(Constant.TypeStatus.DISPONIBLE.getType());
		}
	}

	public void accreditedSolicitude(Project project, Solicitude solicitude, Process process) {
		project.setIdAccredited(solicitude.getAccredited().getHash());
		project.setSolicitude(solicitude);
		project.setProcess(process);
		project.setCurrentStatus(Constant.TypeStatus.LICITACION.getType());
	}
	
	public void notAccreditedSolicitude(Project project, Solicitude solicitude) {
		project.setSolicitude(solicitude);
		project.setCurrentStatus(Constant.TypeStatus.DEVUELTO.getType());
	}
	
}
