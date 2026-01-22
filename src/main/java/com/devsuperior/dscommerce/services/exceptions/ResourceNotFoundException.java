package com.devsuperior.dscommerce.services.exceptions;

public class ResourceNotFoundException extends RuntimeException{//RuntimeException é não checada. Não exige tratamento obrigatório com try catch ou throws
	
	//Construtor recebe uma String como parâmetro e repassa essa String mensagem
	//Na hora de eu instanciar essa exceção, tenho que passar uma String como parâmetro.
	public ResourceNotFoundException (String msg) {
		super(msg);
	}

}
