package com.devsuperior.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;

@Service
public class AuthService {

	@Autowired
	private UserService userService;
	
	//passo um id de usuário como parâmetro (que é o id do pedido), se o usuário que estiver logado não for admin e não for o mesmo do usuário do parâmetro, ou seja ele não é o dono do pedido, vai ser lançada a exceção forbidden
	public void validateSelfOrAdmin(long userId) {
		User me = userService.authenticated();
		if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
			throw new ForbiddenException("Access denied");
		}
	}
}
