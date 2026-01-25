package com.devsuperior.dscommerce.controllers.handlers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscommerce.dto.CustomError;
import com.devsuperior.dscommerce.dto.ValidationError;
import com.devsuperior.dscommerce.services.exceptions.DataBaseException;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

//classe para capturar exceções lançadas pelo service e tratar essas exceções
@ControllerAdvice
public class ControllerExceptionHandler {

	//Qual exceção quero interceptar
	//o service vai lançar o ResourceNotFoundException (quando passado um id que não existe o produto) e capturo aqui e dou esse tratamento abaixo e já respondo ao Front end. Não passa pelo ProductController
	//Método que vai tratar a exceção ResourceNotFoundException
	@ExceptionHandler(ResourceNotFoundException.class)	
	public ResponseEntity<CustomError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND; //NOT_FOUND é o código 404
			//instanciar a class CustomError para fornecer os atributos do erro que aparece para o usuário no postman
		CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI()); 
			//status é um tipo enum, assim chamo o .value() para converter para um int
			//e.getMessage pega a mensagem da minha exceção personalizada
			//path - a partir do objeto HttpServletRequest que obtenho a url que foi chamada que deu a exceção
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(DataBaseException.class)
	public ResponseEntity<CustomError> dataBaseException(DataBaseException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST; //Nesse caso de erro de banco de dados poderi usar o: 400 Bad request ou talvez 409 Conflict. Utilizei o 400			
		CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI()); 			
		return ResponseEntity.status(status).body(err);
	}
	
	//Tratar a exceção que ocorre com relação à validação
	//Poderia fazer igual às demais acima, criar uma exceção personalizada. Assim, no momento que ocorre a exceção eu capturo e lanço a exceção personalizada
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CustomError> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY; //Esse é o erro 422			
		ValidationError err = new ValidationError(Instant.now(), status.value(), "Dados inválidos", request.getRequestURI()); 			
		//acima tirei e.getMessage(), pois retorna uma menssagem grande, então coloquei apenas "Dados inválidos"
		//Uma observação interessante é que se não quero que retorne ao usuário todos esses atributos acima, 
		//crio um cosntrutor de ValidationError somente com os atributos que quero retornar, será? tenho dúvida, pois todos os atributos são isntanciados com null quando não tem valor. Tem que Testar. Talvez teria que criar outra entidade com menos atributos, como fiz nesse caso que quis aumentar o número de atributos
		
		//adicionar os erros na lista do ValidationError com o método que crie na classe dele
		//err.addError("name", "Nome inválido"); teste que fiz e que aparece na requisição
		//err.addError("price", "Preço inválido");
		
		//percorrer o MethodArgumentNotValidException que contém os erros das annotations de validação que customizei 
		//as exceções geradas conforme as annotations de validação estão no e.getBindingResult().getFieldErrors(); 
		//vai retornar uma lista de erros 
		//fazer um for para percorrer a lista de erros
		for (FieldError erro : e.getBindingResult().getFieldErrors()) {
			err.addError(erro.getField(), erro.getDefaultMessage());
		}		
		return ResponseEntity.status(status).body(err);//na hora de eu passar o err no body na resposta à requsição é passado também a lista de erros
	}
		
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<CustomError> forbiddenException(ForbiddenException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.FORBIDDEN; //Nesse caso retorno o código 403 - Forbidden (acesso proibido, apesar de ter feito o login, não tem acesso ao recurso
		CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI()); 			
		return ResponseEntity.status(status).body(err);
	}
}
