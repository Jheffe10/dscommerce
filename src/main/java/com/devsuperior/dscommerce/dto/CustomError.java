package com.devsuperior.dscommerce.dto;

import java.time.Instant;

//Crio um objeto com o quatros argumentos do erro que parece no Postman quando faço um get e passo um ID inexistente
public class CustomError {
	
	private Instant timestamp;
	private Integer status;
	private String error;
	private String path;
	
	//Como coloquei só os getters, não tem porque colocar o construtor padrão, pois não vou conseguir setar os valores nos atributos
	public CustomError(Instant timestamp, Integer status, String error, String path) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.path = path;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}

	public String getPath() {
		return path;
	}	

}
