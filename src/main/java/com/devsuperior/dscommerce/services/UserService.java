package com.devsuperior.dscommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscommerce.dto.UserDTO;
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
			throw new UsernameNotFoundException("Email not found");//caso não encontre o usuário no banco de dados, lanço essa exceção
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
	
	protected User authenticated() {
		
		try {
			//as três linhas abaixo retornam o username(email) de quem está logado no sistema
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //coloco nessa variável os usuários autenticados
			Jwt jwtPrincipal = (Jwt) authentication.getPrincipal(); //pego o objeto jwt
			String username = jwtPrincipal.getClaim("username");//no objeto jwt há os claims (nesse caso são authorities e username), pego o username do token que é o e-mail
			
			User user = repository.findByEmail(username).get(); //vou no banco de dados e pego os dados do usuário logado, através do email do usuário logado. Get para pegar o objeto dentro do optional
			return user;
		}
		catch (Exception e) {
			throw new UsernameNotFoundException("Email not found");
			//Lanço essa exceção caso não encontre o usuário no banco de dados ou der algum erro nas três primeiras linhas do método
		}			
	}
	
	@Transactional(readOnly = true)
	public UserDTO getMe() {
		User user = authenticated(); //peguei o usuário logado com base no token que está guardado no contexto do Spring Security. Peguei os dados dele no banco de dados
		return new UserDTO(user); //converto o usuário do banco de dados para DTO
	}
}
