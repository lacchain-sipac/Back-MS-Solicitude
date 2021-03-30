package com.invest.honduras.util;

public class Constant {

	public static final int STATUS_OK = 00000;
	public static final int STATUS_FAIL = 500;

	public static final String HOST_NOTIFY = System.getenv("MS-NOTIFY");
    public static final String HOST_GATEWAY = System.getenv("MS-API-GATEWAY");
    public static final String API_URL_SEND_NOTIFY = "api/v1/notify/send-mail";

	public static final String MESSAGE_USER_EMAIL_EXIST = "Email ya existe";
    public static final String API_URL_REFRESH_TOKEN= "api/v1/auth/refresh-token";
    
	public static final String MESSAGE_USER_NOT_EXIST = "Usuario no existe";
	public static final String MESSAGE_USER_EMAIL_EMPTY = "Email está vacío";
	public static final String MESSAGE_USER_EMAIL_NOT_EXIST = "Email no existe";
	public static final String MESSAGE_USER_EMAIL_NOT_PERMIT = "Actualizacion no permitida";
	public static final String MESSAGE_USER_ROLE_EMPTY = "Rol está vacío";
	public static final String MESSAGE_USER_ROLE_NOT_PERMIT = "Favor obtener los roles de MS-Parametros , Rol no Permitido ";
	public static final String MESSAGE_USER_SECTION_NOT_PERMIT = "Favor obtener los roles de MS-Parametros , Section Page no Permitido ";
	public static final String MESSAGE_USER_ID_NOT_FOUND = "ID no existe";
	public static final String MESSAGE_USER_PASSWORD_EMPTY = "Password o la confirmación de password es vacío";
	public static final String MESSAGE_USER_TYPE_EMPTY = "Tipo vacio o distinto de COMPLETE_PASSWORD o CHANGE_PASSWORD ";
	public static final String MESSAGE_USER_PASSWORD_NOT_EQUAL = "Password y la confirmación de password son diferentes";
	public static final String MESSAGE_USER_PASSWORD_REGEX = "Password debe contener al menos una mayuscula , una minuscula, caracter especial [!, $, %, @, &] y tamaño entre 12 a 20 caracteres";
	public static final String MESSAGE_USER_EMAIL_EQUAL_PASSWORD = "Password no debe ser igual a email";
	public static final String MESSAGE_SOLICITUDE_ID_NOT_FOUND = "SOLICITUDE_ID_NOT_FOUND";

	public static final String TYPE_COMPLETE_PASSWORD = "COMPLETE_PASSWORD";
	public static final String TYPE_INVITATION_USER = "INVITATION_USER";
	public static final String TYPE_UPDATE_USER = "UPDATE_USER";
	public static final String TYPE_UPDATE_STATE_USER = "UPDATE_STATE_USER";
	public static final String TYPE_FORGET_PASSWORD = "FORGET_PASSWORD";
	public static final String TYPE_CHANGE_PASSWORD = "CHANGE_PASSWORD";
	public static final String MESSAGE_ROLE_NOT_FOUND = "ROLE_NOT_FOUND_BY_USER";
	public static final String MESSAGE_REFRESH_TOKEN_BAD_REQUEST = "BAD REQUEST REFRESH TOKEN";
	public static final String MESSAGE_REFRESH_TOKEN_UNAUTHORIZED =   "UNAUTHORIZED REFRESH TOKEN";
	public static final String MESSAGE_REFRESH_TOKEN_ERROR = "Internal Error";	
	
	public static final String TYPE_REVIEW_APPROVED = "approval";
	public static final String TYPE_REVIEW_OBSERVED = "observation";
	

	public static final String NOTIFY_SOLICITUDE_STEP_4 = "Se ha ingresado la documentación relacionada "
			+ "a la solicitud con nombre %s, sírvase  confirmar la disponibilidad presupuestaria y,"
			+ " en su caso, registrar la estructura presupuestaria de la misma.";

	public static final String NOTIFY_SOLICITUDE_STEP_5 = "Se ha registrado la disponibilidad presupuestaria "
			+ "relacionada a la solicitud " + "con nombre %s, sírvase terminar el registro de la solicitud.";  

	public static final String NOTIFY_SOLICITUDE_STEP_5_FINISH = "La Solicitud del proceso %s está "
			+ "pendiente de su acreditación.";	
	
	public static final String NOTIFY_SOLICITUDE_STEP_5_OPT_FINISH = "La presente solicitud se ha creado sin estructura "
			+ "presupuestaria con la siguiente observación %s.";

	public static final String NOTIFY_SOLICITUDE_ACCREDITED = "Se ha acreditado correctamente la solicitud del proceso %s, "
			+ "ROL Especialista de adquisiciones, por favor vaya a la Bandeja de Entrada y haga clic en la opción Iniciar Proceso de la Fase de PROCESOS.";	
	
	public static final String NOTIFY_SOLICITUDE_NO_ACCREDITED = "La solicitud del proceso %s  no se acreditó debido a: %s.";
	
	/******************************************MENSAJES POR FLUJO DOCUMENTARIO RDELAROSA 06/05/2020**************************************************/
	
	
	/* FASE LICIACION - PASO 1 - DOCUMENTO DE LICITACION */
	
	public static final String NOTIFY_DOC_ATTACH = "Se ha adjuntado el %s %s, "
		      + "del proceso %s que está a la espera de su aprobación.";
	
	public static final String NOTIFY_DOC_BIDDING_ACREDITED = "Se ha aprobado el %s %s,"
		      + "que está a la espera de su acreditación.";

	public static final String NOTIFY_DOC_BIDDING_FINALLY = "Se ha acreditado el %s %s,"
		      + "que está a la espera de subir el documento final.";

	public static final String NOTIFY_DOC_BIDDING_NO_OBJECTION = "Se ha subido la versión final del %s %s ,"
		      + "del proceso %s, favor adjuntar la solicitud y respuesta de No Objeción al BID.";
	
	public static final String NOTIFY_DOC_BIDDING_RESPONSE_NO_OBJECTION = "Se ha adjuntado la solicitud y respuesta de No Objecion al BID del %s %s ," 
			  +  "del proceso %s, favor sírvase a registrar el Acta de Apertura.";
	
	
	/* FIN DE DOCUMENTO DE LICITACION */
	
	
	/* FASE LICITACION - PASO 1 - DOCUMENTO DE ACLARATORIA */
	
	
	public static final String NOTIFY_DOC_CLARIFICATION_REQUEST = "Se ha adjuntado el %s %s, se está "
			+ "a la espera de que adjunte la nota de aclaratoria.";
	
	public static final String NOTIFY_CLARIFICATION_RESPONSE = "Se ha adjuntado el %s %s, se está "
			+ "a la espera de que apruebe la nota de aclaratoria.";
	
	public static final String NOTIFY_CLARIFICATION_ACREDITED = "Se ha aprobado el %s %s,"
		      + "que está a la espera de su acreditación.";
	
	public static final String NOTIFY_CLARIFICATION_FINALLY = "Se ha acreditado el %s %s,"
		      + "que está a la espera de subir el documento final.";

	/* FASE LICITACION - PASO 1 - DOCUMENTO DE ENMIENDA */
	
	public static final String NOTIFY_AMENDMENT_REQUEST = "Se ha adjuntado el %s %s, se está a "
			+ "la espera de que adjunte el borrador de enmienda.";

	
	public static final String NOTIFY_AMENDMENT_RESPONSE = "Se ha adjuntado el %s %s, se esta a"
			+ "la espera de que apruebe el borrador de enmienda.";

	
	public static final String NOTIFY_AMENDMENT_ACREDITED = "Se ha aprobado el %s %s,"
		      + "que está a la espera de su acreditación.";
	
	public static final String NOTIFY_AMENDMENT_FINALLY = "Se ha acreditado el %s %s,"
		      + "que está a la espera de subir el documento final.";
	
	
	public static final String NOTIFY_AMENDMENT_RESPONSE_APPROVE = "Se ha subido la version final del "
			+ "%s %s, favor adjuntar la Solicitud y Respuesta de No objeción al BID.";
	
	
	public static final String NOTIFY_AMENDMENT_RESPONSE_NO_OBJECTION = "Se ha adjuntado la solicitud y respuesta de No Objecion al BID del %s %s ," 
			  +  "del proceso %s, favor sírvase a registrar el Acta de Apertura.";
	
	/*.............................................................................................................*/
	
	
	/* FASE LICITACION - PASO 2  QUITAR RDELAROSA */
	
	/*
	 * public static final String NOTIFY_DOC_ACT_OPPENING =
	 * "Se ha aprobado el documento %s con nombre %s, " +
	 * "que está a la espera de su aprobación“";
	 * 
	 * public static final String NOTIFY_DOC_ACT_OPPENING_APPROVE =
	 * "Se ha aprobado el Acta de Apertura favor sírvase " +
	 * "a continuar con el siguiente paso “Comité de Evaluación.”";
	 */
	
	/*............................................................................................................*/
	
	
	/* FASE LICITACION - PASO 4 */
	
	public static final String NOTIFY_DOC_EVALUATION_REPORT = "Se ha adjuntado el %s %s, "
		      + "del proceso %s que está a la espera de su aprobación.";
	
	
	public static final String NOTIFY_DOC_EVALUATION_REPORT_ACREDITED = "Se ha aprobado el %s %s,"
		      + "que está a la espera de su acreditación.";

	public static final String NOTIFY_DOC_EVALUATION_REPORT_FINALLY = "Se ha acreditado el %s %s,"
		      + "que está a la espera de subir el documento final.";
	
	
	public static final String  NOTIFY_DOC_EVALUATION_REPORT_NO_OBJECTION = "Se ha subido la versión final del %s %s ,"
		      + "del proceso %s, favor adjuntar la solicitud y respuesta de No Objeción al BID.";
	
	public static final String NOTIFY_DOC_EVALUATION_REPORT_RESPONSE_NO_OBJECTION = "Se ha adjuntado la solicitud y respuesta de No Objecion al BID del %s %s ," 
			  +  "del proceso %s, favor sírvase confirmar el siguiente paso Carga Estructura Presupuestaria.";
	
	/*.......................................................................................................................*/


	/* FASE LICITACION - PASO 6 */
	
	public static final String NOTIFY_DOC_AWARD_RESOLUTION = "Se ha adjunta el documento %s con nombre %s, "
			+ "que está a la espera de su aprobación";

	public static final String NOTIFY_DOC_AWARD_RESOLUTION_APPROVE = "Se ha aprobado la Resolución de Adjudicación "
			+ "favor sírvase a continuar con el siguiente paso “Contrato firmado\".";
	
	
	/*...........................................................................................................*/
	
	
	/* FASE LICITACION - PASO 7 */

	public static final String NOTIFY_DOC_SIGNED_CONTRACT = "Se ha adjuntado el %s %s, "
		      + "del proceso %s que está a la espera de su aprobación.";
	
	public static final String NOTIFY_DOC_SIGNED_CONTRACT_ACREDITED = "se ha aprobado el %s %s,"
		      + "que está a la espera de su acreditación.";

	public static final String NOTIFY_DOC_SIGNED_CONTRACT_FINALLY = "Se ha acreditado el %s %s,"
		      + "que está a la espera de subir el documento final.";
	
	
	public static final String NOTIFY_EXECUTE_INIT = "Se ha transferido correctamente la solicitud con nombre"
			+ " %s, para que inicie el registro de los pagos por lo que ahora la solicitud se encuentra en la Fase de Ejecución.";

	
	
	/* FASE EJECUCION - PASO 3 - SOLICITUD DE ANTICIPO  ADD RDELAROSA 23/05/2020 */
	
	public static final String NOTIFY_DOC_ADVANCE_REQUEST = "Se ha adjuntado la %s %s, "
		      + "del proceso %s que está a la espera de su aprobación.";
	
	
	public static final String NOTIFY_DOC_ADVANCE_REQUEST_ACREDITED = "Se ha aprobado la %s %s,"
		      + "que está a la espera de su acreditación.";

	public static final String NOTIFY_DOC_ADVANCE_REQUEST_FINALLY = "Se ha acreditado la %s %s,"
		      + "que está a la espera de subir el documento final.";
	
	
	public static final String  NOTIFY_DOC_ADVANCE_REQUEST_F01_CDP = "Se ha adjuntado la versión final de la %s %s ,"
		      + "del proceso %s, favor adjuntar el F01 y Confirmación de Pago asociado.";
	
	public static final String  NOTIFY_DOC_ADVANCE_REQUEST_PROOF = "Se ha adjuntado el F01 y Confirmación de Pago de la %s %s ,"
		      + "del proceso %s, en caso no haber más solicitudes de Anticipo, favor continue con la Solicitud de Estimación de Pago asociado.";
	
	
	
	/* ...........................................................................................*/
	
	
	/* FASE EJECUCION - PASO 3 ESTIMACION DE PAGO  ADD RDELAROSA 23/05/2020 */
	
	public static final String NOTIFY_DOC_ESTIMATE_REQUEST = "Se ha adjuntado la %s %s, "
		      + "del proceso %s que está a la espera de su aprobación.";
	
	
	public static final String NOTIFY_DOC_ESTIMATE_REQUEST_ACREDITED = "Se ha aprobado la %s %s,"
		      + "que está a la espera de su acreditación.";

	public static final String NOTIFY_DOC_ESTIMATE_REQUEST_FINALLY = "Se ha acreditado la %s %s,"
		      + "que está a la espera de subir el documento final.";
	
	
	public static final String  NOTIFY_DOC_ESTIMATE_REQUEST_F01_CDP = "Se ha adjuntado la versión final de la %s %s ,"
		      + "del proceso %s, favor adjuntarel F01 y Confirmación de Pago asociado.";
	
	public static final String  NOTIFY_DOC_ESTIMATE_REQUEST_PROOF = "Se ha adjuntado el F01 y Confirmación de Pago de la %s %s ,"
		      + "del proceso %s, en caso no haber más estimaciones de Pago relacionadas, favor continue con la Solicitud de Pago Final asociada.";
	
	
	/* QUIT RDELAROSA 23/05/2020 */
	
	/*public static final String NOTIFY_DOC_ADVANCE_REQUEST_APPROVE = "Se ha aprobado el Anticipo de Pago, "
			+ "es necesario que pueda adjuntar el F-01 asociado.";

	public static final String NOTIFY_DOC_ESTIMATE_REQUEST_APPROVE = "Se ha aprobado el Anticipo de Pago, "
			+ "es necesario que pueda adjuntar el F-01 asociado.";

     ....................................................................................................*/
	

	/* FASE EJECUCION - PASO 3 PAGO FINAL  ADD RDELAROSA 23/05/2020 */

	public static final String NOTIFY_DOC_FINAL_PAYMENT = "Se ha adjuntado la %s %s, "
		      + "del proceso %s que está a la espera de su aprobación.";
	
	
	public static final String NOTIFY_DOC_FINAL_PAYMENT_ACREDITED = "Se ha aprobado la %s %s,"
		      + "que está a la espera de su acreditación.";

	public static final String NOTIFY_DOC_FINAL_PAYMENT_FINALLY = "Se ha acreditado la %s %s,"
		      + "que está a la espera de subir el documento final.";
	
	
	public static final String  NOTIFY_DOC_FINAL_PAYMENT_F01_CDP = "Se ha adjuntado la versión final de la %s %s ,"
		      + "del proceso %s, favor adjuntar el F01 y Confirmación de Pago asociado.";
	
	public static final String  NOTIFY_DOC_FINAL_PAYMENT_PROOF = "Se ha adjuntado el F01 y Confirmación de Pago de la %s %s ,"
		      + "del proceso %s, en caso no haber más pagos finales relacionadas ni modificacion de contrato, favor continue con el siguiente paso \"Cierre Contrato\".";


	/* FASE EJECUCION - PASO 3 MODIFICACION DE CONTRATO  ADD RDELAROSA 25/05/2020 */
	
	public static final String NOTIFY_DOC_CONTRACT_MODIFICATE = "Se ha adjuntado la %s %s, "
		      + "del proceso %s que está a la espera de su aprobación.";
	
	public static final String NOTIFY_DOC_CONTRACT_MODIFICATE_ACREDITED = "Se ha aprobado la %s %s,"
		      + "que está a la espera de su acreditación.";

	public static final String NOTIFY_DOC_CONTRACT_MODIFICATE_FINALLY = "Se ha acreditado la %s %s,"
		      + "que está a la espera de subir el documento final.";
	
	/*
	public static final String NOTIFY_DOC_CONTRACT_MODIFICATE_APPROVE = "Se ha aprobado el documento %s con nombre %s, "
			+ "se está a la espera de que adjunte su No Objeción al BID";
     */

	/******************************************MENSAJES POR FLUJO DOCUMENTARIO RDELAROSA 06/05/2020**************************************************/
	
	
	/*****************************************MENSAJES POR STEPS ************************************************************************************/
	
    /* FASE 2 - PASO 1 - ADD RDELAROSA 06/05/2020*/
	public static final String NOTIFY_STEP_INIT_PROCCESS = "Se ha adjuntado y acreditado el Documento de Licitación y sus documentos asociados"
			+ " del proceso  %s, favor sírvase a registrar el Acta de Apertura. ";
	
	/* FASE 2 - PASO 2 - ADD RDELAROSA 06/05/2020*/
	public static final String NOTIFY_STEP_OFFER_DOCUMENTATION = "Se ha adjuntado y acreditado el Acta de apertura"
			+ " del proceso %s, favor sírvase a designar al Comité de Evaluación. ";

	/* FASE 2 - PASO 3 - ADD RDELAROSA 06/05/2020*/
	public static final String NOTIFY_STEP_COMMITEE = "Se ha designado el comité de evaluación "
			+ "del proceso %s, favor sírvase a adjuntar el informe de evaluación.";
	
	/* FASE 2 - PASO 4 - ADD RDELAROSA 06/05/2020*/
	public static final String NOTIFY_STEP_EVALUATION_REPORT="Se ha adjuntado y acreditado el informe de evaluación "
			+ "asi como sus documentos asociados al Proceso %s, favor sírvase confirmar "
			+ "el siguiente paso Carga Estructura Presupuestaria.";
	
	/* FASE 2 - PASO 5 - ADD RDELAROSA 06/05/2020*/
	public static final String NOTIFY_STEP_STRUCTURE_BUDGET = "Se ha confirmado la estructura presupuestaria del proceso %s, "
			+ "favor sirvase a continuar con el siguiente paso Resolución de Adjudicación.";
	
	
	/* FASE 2 - PASO 6 - ADD RDELAROSA 06/05/2020*/ 
	public static final String NOTIFY_STEP_AWARD_RESOLUTION = "Se ha adjuntado y acreditado la Resolución de Adjudicación "
			+ "del proceso %s, favor sírvase adjuntar el Contrato Firmado.";
	
	/* FASE 2 - PASO 7 - ADD RDELAROSA 06/05/2020*/
	public static final String NOTIFY_STEP_SIGNED_CONTRACT = "Se ha concluido con la Fase PROCESOS del proceso %s, "
			+ "puede iniciar la Fase EJECUCION.";

	
	/* FASE 3 - PASO 4 - ADD RDELAROSA 25/05/2020*/
	public static final String NOTIFY_STEP_MODIFY_CONTRACT = "Se ha adjuntado los pagos y sus documentos relacionados del proceso %s"
			+ " por lo que está pendiente terminar la fase de Pagos.";
	
	/* QUIT RDELAROSA 25/05/2020
	public static final String NOTIFY_STEP_PAYMENT = "El número de Solicitud con nombre de proceso %s"
			+ " está pendiente que se adjunte la modificación de contrato.";
	*/
	
	/* FASE 3 - PASO 5 - ADD RDELAROSA 25/05/2020*/
	public static final String NOTIFY_STEP_STEP_QUALITY_GUARANTEE = "El proceso %s está a la espera "
			+ "de que se pueda terminar el ciclo financiero del proceso.";
	
	
	
	/*****************************************MENSAJES POR STEPS ************************************************************************************/
	
	
	
	
	public static final String NOTIFY_PROJECT_FINISH = "Se ha terminado el ciclo financiero de la solicitud con nombre %s.";
	
	/* MENSAJE DE  APROBACION GENERAL*/
	public static final String NOTIFY_DOC_APPROVE_GENERAL = "Se ha aprobado el %s %s, "  
					      + "que esta a la espera de su aprobación";

	/* MENSAJE DE  OBSERVACION GENERAL ADD RDELAROSA 13/08/2020*/
	public static final String NOTIFY_DOC_OBSERVED_GENERAL = "Se ha observado el %s %s, "  
		      + "es necesario reemplazarlo";
	
	public static final String TYPE_TEMPLATE = "general";

	public static final String NOTIFY_SUBJET = "Proceso Fiduciario Invest-H";

	public static final short STEP_FIVE = 5;

	public static final String FILE_STEP_CODE = "codeStep";
	public static final String FILE_ID_PROJECT = "idProject";
	public static final String FILE_DOCUMENT_TYPE = "documentType";
	public static final String FILE_DOCUMENT_ACCREDITED = "accredited";
	public static final String FILE_DOCUMENT_OBSERVATION = "observation";
	public static final String FILE_DOCUMENT_SIGNED_CONTRACT = "signedContract";
	public static final String FILE_DOCUMENT_ID_GROUP = "idGroup";
	public static final String HOST_BLOCKCHAIN = System.getenv("MS-BLOCKCHAIN");
	public static final String API_URL_BLOCKCHAIN_PROJECT = "/api/v1/blockchain/project";
	public static final String API_URL_BLOCKCHAIN_DOCUMENT = "/api/v1/blockchain/document";
	public static final String API_URL_BLOCKCHAIN_COMMENT = "/api/v1/blockchain/comment";
	public static final String API_URL_BLOCKCHAIN_PROJECT_ROLE = "/api/v1/blockchain/role-project";
	public static final String API_URL_BLOCKCHAIN_HAS_USER_PROJECT_ROLE = "/api/v1/blockchain/user-role-project";
	public static final String API_URL_BLOCKCHAIN_HAS_USER_ROLE = "/api/v1/blockchain/user-role";
	
	public static enum TypeStatus {
		INICIADO("I"), DISPONIBLE("D"), DEVUELTO("R"), EJECUCION("E"), LICITACION("L"), TERMINADO("T");

		private final String type;

		TypeStatus(String status) {
			type = status;
		}

		public String getType() {
			return type;
		}

	}

	public static final String USER_POR_COMPLETAR = "P";
	public static final String USER_HABILITADO = "H";

	public static final String REGEX = "^(?=.*\\d)(?=.*[\\u0021-\\u002b\\u003c-\\u0040])(?=.*[A-Z])(?=.*[a-z])\\S{12,20}$";
	
	public static final String ROLE_ADMIN = "ROLE_1";
	public static final String ROLE_COO_TEC = "ROLE_COO_TEC";
	
	
	public static final String URL_SOLICITUDE_STEP_5 = "/home/processes/%s/request/step-five";
	
	public static final String URL_HOME_PROCESS = "/home/processes/";
	
	public static final String URL_PROCESS_STEP_1 = "/home/processes/%s/process/step-one";
	public static final String URL_PROCESS_STEP_2 = "/home/processes/%s/process/step-two";
	public static final String URL_PROCESS_STEP_3 = "/home/processes/%s/process/step-three";
	public static final String URL_PROCESS_STEP_4 = "/home/processes/%s/process/step-four";
	public static final String URL_PROCESS_STEP_5 = "/home/processes/%s/process/step-five";
	public static final String URL_PROCESS_STEP_6 = "/home/processes/%s/process/step-six";
	public static final String URL_PROCESS_STEP_7 = "/home/processes/%s/process/step-seven";

	
	public static final String URL_EXECUTE_STEP_1 = "/home/processes/%s/execution/step-one";
	public static final String URL_EXECUTE_STEP_2 = "/home/processes/%s/execution/step-two";
	public static final String URL_EXECUTE_STEP_3 = "/home/processes/%s/execution/step-three";
	public static final String URL_EXECUTE_STEP_4 = "/home/processes/%s/execution/step-four";
	public static final String URL_EXECUTE_STEP_5 = "/home/processes/%s/execution/step-five";
	public static final String URL_EXECUTE_STEP_6 = "/home/processes/%s/execution/step-six";
}
