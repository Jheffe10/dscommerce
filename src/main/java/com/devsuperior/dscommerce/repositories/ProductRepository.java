package com.devsuperior.dscommerce.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.dscommerce.entities.Product;

//Long é o tipo do identificador (ID) da entidade Product.
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	//Método customizado - SPQL - Quero fazer uma pesquisa pelo nome do produto, inclusive podendo passar parte do nome
	//Como estou utilizando um método personalizado devo colocar a minha consulta
	@Query("Select obj From Product obj "
			+ "WHERE UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%'))")
	Page<Product> searchByName(String name, Pageable pageable);
}
