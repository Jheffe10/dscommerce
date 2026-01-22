package com.devsuperior.dscommerce.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;

@RestController
@RequestMapping(value = "/productsTest") //recurso products sendo informado da sua a rota
public class ProductControllerTest {
	
	//injetar componente do repository
	@Autowired
	private ProductRepository repository;
	
	//nessa rota se eu chamar o método GET executa o método abaixo
	@GetMapping
	public String test() {
		Optional<Product> result = repository.findById(1L); //findById retorna um Optional, e nesse caso, com um product dentro
		Product product = result.get(); //para pegar o objeto dentro do Optional
		return product.getName(); //vai retornar o nome do produto
	}

}
