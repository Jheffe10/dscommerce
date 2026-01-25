package com.devsuperior.dscommerce.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends CustomError{
	
	private List<FieldMessage> errors = new ArrayList<>();
	
	public ValidationError(Instant timestamp, Integer status, String error, String path) {
		super(timestamp, status, error, path);		
	}

	public List<FieldMessage> getErrors() {
		return errors;
	}
	
	//método para adicionar message à lista
	public void addError(String fieldName, String message) {
		errors.removeIf(x -> x.getFieldName().equals(fieldName)); //remove o erro caso seja o mesmo que quero adicionar
		
		FieldMessage fm = new FieldMessage(fieldName, message);
		errors.add(fm);
		//error.add(new FieldMessage(fieldName, message)); assim fica melhor do que declarar as duas linhas acima
	}

	

}
