package com.invest.honduras.http.response;

import java.util.Date;

import com.invest.honduras.domain.model.solicitude.StatusSolicitude;
import com.invest.honduras.http.response.execution.ExecuteItemResponse;
import com.invest.honduras.http.response.process.ProcessItemResponse;
import com.invest.honduras.http.response.solicitude.SolicitudeItemResponse;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectItemResponse {
	@ApiModelProperty(notes = "Inditificador del proyecto", example = "5f5970b686de650001c745fb")
	private String id;
	
	@ApiModelProperty(notes = "Detalle de la solicitud del proyecto", example = "")
	private SolicitudeItemResponse solicitude;
	
	@ApiModelProperty(notes = "Detalle del proceso del proyecto", example = "")
	private ProcessItemResponse process;
	
	@ApiModelProperty(notes = "Detalle de la ejecución del proyecto", example = "")
	private ExecuteItemResponse execution;

	@ApiModelProperty(notes = "Usuario que crea la solicitud", example = "annyhome@gmail.com")
	private String createdBy;

	@ApiModelProperty(notes = "fecha de creación de la solicitud", example = "2020-09-10T00:17:58.204+0000")
	private Date createdDate;

	@ApiModelProperty(notes = "Usuario que modifica la solicitud", example = "annyhome@gmail.com")
	private String lastModifiedBy;

	@ApiModelProperty(notes = "fecha de modificación de la solicitud", example = "2020-09-10T02:39:03.969+0000")
	private Date lastModifiedDate;
	
	@ApiModelProperty(notes = "Estado actual del proyecto", example = "")
	private StatusSolicitude currentStatus;
}
