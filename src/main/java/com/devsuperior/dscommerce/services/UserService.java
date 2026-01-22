package com.devsuperior.dscommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.devsuperior.dscommerce.entities.Role;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.projection.UserDetailsProjection;
import com.devsuperior.dscommerce.repositories.UserRepository;

//UserDetailsService é uma das interfaces necessárias para criptografar a senha do usuário
@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
		if (result.size() == 0) {
			throw new UsernameNotFoundException("Email not found");//caso não encontreo usuário no banco de dados, lanço essa exceção
		}						//essa exceção ajuda a gerenciar o processo de autenticação de usuário
		
		//se encontrou o username no banco de dados vou instanciar o user
		User user = new User();
		user.setEmail(result.get(0).getUsername());
		user.setPassword(result.get(0).getPassword());
		for (UserDetailsProjection projection : result) { //para pegar os objetos roles do usuário
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));//utilizo o método que criei addRole para adicionar os roles ao user
		}
		
		return user;
	}
}
