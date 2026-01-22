package com.devsuperior.dscommerce.dto;

public class FieldMessage {
	
	//classe para retorna o message para o usuário referente às annotations de Validação no ProductDTO
	private String fieldName;
	private String message;
	
	
	public FieldMessage(String fieldName, String message) {		
		this.fieldName = fieldName;
		this.message = message;
	}


	public String getFieldName() {
		return fieldName;
	}


	public String getMessage() {
		return message;
	}
	
	
	

}
