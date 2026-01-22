package com.devsuperior.dscommerce.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//No final criei a classe ResourceServerConfig e migrei esses métodos para lá
//Assim essa classe ficou dispensável
@Configuration
public class SecurityConfig {

	//Nessa classe de configuração defino componentes na forma de métodos.
	//Dentro dessa classe configuro componentes: @Bean...
	
	//Configurar a ciptografia da senha (password encode)
	@Bean
	public PasswordEncoder getPassowrdEncoder() { //PassworEncoder é a interface, um tipo genérico. Assim posso utilizar qualquer implementação dessa interface. Uma delas é o BCrypt
		return new BCryptPasswordEncoder(); //Só retornar um new que funciona, instancia o objeto e como tem o @Bean o objeto vira
								//um componente que pode ser injetado (através do @Autowired) em outros lugares
	}
	
	
	//Filtro do Spring Security para pode acessar o banco H2, caso contrário o acesso não é liberado
	@Bean
	@Profile("test") //perfil de teste
	@Order(1) //ordem do filtro, primeiro executa esse e depois o de baixo
	SecurityFilterChain h2SecurityFilterChain(HttpSecurity http) throws Exception {

		http.securityMatcher(PathRequest.toH2Console()).csrf(csrf -> csrf.disable())
				.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
		return http.build();
	}
	
	//Como adicionei as dependências do security no projeto (no arquivo .pom) já não consigo mais acessar os endpoints
	//Aí posso liberar provisoriamente os endpoints através da criação dessa classe
	//Com isso não ocorre mais o erro 401 - falta de autorização e os endpoints ficam liberados
	@Bean
	@Order(2) //ordem do filtro
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable());//desabilitando ataques do tipo csrf
		http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());//permissões para os meus endpoints (requisições). Estou permitindo tudo para qualquer requisição
										//e aí as rotas que preciso de restrição configuro a nível de rota (controle de aceso por rota). Poderia fazer aqui, mas aqui é mais geral
		return http.build();
	}
}


