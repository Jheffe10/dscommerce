package com.devsuperior.dscommerce.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.devsuperior.dscommerce.entities.User;

import jakarta.persistence.Column;

public class UserDTO {
	
	//esse DTO faço sem a senha
	private Long id;
	private String name;	
	private String email; //e-mail vai ser o username para fazer o login na aplicação
	private String Phone;
	private LocalDate birthDate;
	
	
	//Como me interessa só o nome dos roles, posso fazer uma lista de roles
	private List<String> roles = new ArrayList();

	public UserDTO(User entity) {		
		id = entity.getId();
		name = entity.getName();
		email = entity.getEmail();
		Phone = entity.getPhone();
		birthDate = entity.getBirthDate();		
		for (GrantedAuthority role : entity.getRoles()) {
			roles.add(role.getAuthority());
		}			
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return Phone;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public List<String> getRoles() {
		return roles;
	}	

}
