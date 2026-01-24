package com.devsuperior.dscommerce.dto;

import java.util.ArrayList;
import java.util.List;

import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


public class ProductDTO {
	
	private Long id;
	
	//colocar as validações da biblioteca Bean Validation
	@Size(min = 3, max = 80, message = "Nome precisa ter entre 3 e 80 caracteres" ) //essa annotation limita o tamanho. Ela tem vários argumentos
	@NotBlank(message = "Campo requerido") //campo deverá obrigatoriamente ser preenchido. Essa annotation é a mais completa engloba a @NotNull (não pode ser nula), a @NotEmpty (não pode ser vazia "") e também não aceita só espaços em branco "   "
				//essa mensage retorna para o usuário se ele não atender os requisitos
	private String name;	
	
	@Size(min = 10, message = "Descrição precisa ter no mínimo 10 caracteres" )
	@NotBlank(message = "Campo requerido")
	private String description;
	
	@Positive(message = "O preço deve ser positivo")
	private Double price;
	private String imgUrl;
	
	@NotEmpty(message= "Deve haver pelo menos uma categoria") //na hora de inserir ou atualizar um produto deve haver pelo menos uma categoria para o produto
	private List<CategoryDTO> categories = new ArrayList<>();
	
	//tive que deixar esse cosntrutor padrão, pois dava erro no update para um número inválido. Não estava capturando a exceção personalizada
	public ProductDTO() {		
	}	
	
	public ProductDTO(Long id, String name, String description, Double price, String imgUrl) {		
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
	}
	
	//Construtor abaixo facilita para transformar um Product em ProductDTO
	//Ou seja, depois de acessar o banco de dados e receber um Product, na camada de service tenho
	//que transformar o product em productDTO, ou seja, passar os valores dos atributos de Product para o ProductDTO
	public ProductDTO(Product product) {		
		this.id = product.getId();
		this.name = product.getName();
		this.description = product.getDescription();
		this.price = product.getPrice();
		this.imgUrl = product.getImgUrl();
		for (Category cat : product.getCategories()) { //adicionei também a categoria na hora de inserir ou adicionar um produto novo deve ser informada a categoria do produto
			categories.add(new CategoryDTO(cat));
		}
	}
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Double getPrice() {
		return price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}	

}
