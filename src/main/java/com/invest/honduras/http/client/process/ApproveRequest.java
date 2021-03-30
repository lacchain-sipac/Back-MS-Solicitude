package com.invest.honduras.http.client.process;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApproveRequest {
	@ApiModelProperty(notes = "Indentificador del proyecto", example = "5ecc3646fecab200012b6543")
	private String idProject;
	
	@ApiModelProperty(notes = "Indica el paso o nivel en el proyecto" , example = "paso_03_02")	
	private String codeStep;
	
	@ApiModelProperty(notes = "Indica el tipo del documento" , example = "doc_ass_03_03_03_01")	
	private String documentType;
	
	@ApiModelProperty(notes = "Identificador del documento" , example = "5f03bec8d1ffdd0001e73d78")	
	private String idStorage;
	
	@ApiModelProperty(notes = "Observación del documento anexado" , example = "documento de pago")	
	private String observation;
	
	@ApiModelProperty(notes = "El tipo de acción" , example = "upload")	
	private String type;
	
	@ApiModelProperty(notes = "Nombre del documento" , example = "documento1901.pdf")	
	private String nameFile;
	
	@ApiModelProperty(notes = "Número de aprobación" , example = "1")	
	private String nroApproved;
	
	@ApiModelProperty(notes = "Rol del usuario" , example = "ROLE_DIR_ADM_FIN")	
	private String role;
	
	@ApiModelProperty(notes = "Descripción del Rol" , example = "Administrador")	
	private String roleName;
	
}
