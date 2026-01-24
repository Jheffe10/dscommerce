package com.devsuperior.dscommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscommerce.dto.CategoryDTO;
import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.DataBaseException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;



@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Transactional(readOnly=true)
	//Método que vai no banco de dados, busca o produto por id e converte para um DTO (ProductDTO) e retorna
	//Tudo isso pois o service retorna um DTO para o controller
	public ProductDTO findByID(Long id) {
		
		//Alterei o código abaixo para tratar uma exceção caso não exista um produto com o id pesquisado
		//Optional<Product> result = repository.findById(id); //findById retorna um Optional, e nesse caso, com um product dentro
					//observe que o argumento id vem com a requisição
					//Fui no banco de dados, busquei o produto do id requerido e retornei para a variável result
		
		//Alterei o código abaixo para tratar uma exceção caso não exista um produto com o id pesquisado		
		//Product product = result.get(); //para pegar o objeto dentro do Optional
		
		//tratando a exceção, caso não exista produto com o id passado no parâmetro
		//Product product = result.orElseThrow(() -> new ResourceNotFoundException("Id não existe"));
		
		//Com o orElseThrow() não preciso fazer aqui no get um try/catch
		//Fica melhor colocar tudo em uma linha
		Product product = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id não existe"));
		//intercepto a exceção e coloco a minha personalizada. No postman vai continuar o erro 500
		//Porém no console do eclipse aparece a minha exceção (quando quebra a aplicação). Foi lançada a minha exceção. Preciso tratá-la para não quebrar a aplicação
		//Se o objeto Optional tem valor, retorna. Se não tem, lança a exceção ResourceNotFoundException com a mensagem ‘Id não existe’
		//vou tratá-la lá no controller (com o try/catch no método findById) ou com o @ControllerAdvice
		//Devo tratá-la, ou seja, capturá-la, tratar e devolver uma mensagem personalizada no Postman
		
		//próximo passo é transformar o Product em ProductDTO. Lembrar que no ProductDTO temos os atributos 
		//que podem ter todos ou parte de Product. Devo primeiro criar um productDTO, para isso instancio ele
		// Na mão teria que fazer isso abaixo, passando cada atributo de product para o DTO
		//ProductDTO new ProductDTO(product.getId(), product.getName(),...)
		//Para facilitar, crio outro construtor no DTO em que eu passo como parâmetro a entidade Product
		ProductDTO dto = new ProductDTO(product);
		return dto;
	}
	
	//Abaixo é uma busca não paginada
	//Fazer uma busca de todos os produtos, tenho que retornar todos os produtos. Esse é um método de serviço que busca todos os produtos
	//Da mesma forma que anteriormente, tenho que retornar um DTO, ou seja, converter o Product vindo do repository em ProductDTO
	//Nesse caso vai converter uma lista de Product em lista de ProductDTO	
	@Transactional(readOnly=true)
	public List<ProductDTO> findAllNaoPaginada() {
		List<Product> result = repository.findAll();
		//No map, para cada elemento de x de result(result é uma lista de Product e cada elemento dele é um Product) e instancio 
		//um ProductDTO. Para isso utilizo o construtor em que passo um objeto Product como parâmetro
		List<ProductDTO> listDto = result.stream().map(x -> new ProductDTO(x)).toList();
		return listDto;		
	}
	
	
	//Abaixo é uma busca paginada. Recebo o argumento Pageable vindo do controller	
	//Também adaptei para receber como parâmetro o nome do produto ou parte do nome
	@Transactional(readOnly=true)
	public Page<ProductMinDTO> findAll(String name, Pageable pageable) {
		//Repasso o Pageable para o repository. Há um findAll que recebe um pageable e return um Page<Product>
		//Page<Product> result = repository.findAll(pageable);
		//Comentei acima para utilizar um método personalizado abaixo
		Page<Product> result = repository.searchByName(name, pageable);
		//Devo converter um Page<Product> para Page<ProductDTO>. O Page é um stream, não preciso converter para stream(), aceita direto o map
		//No map, para cada elemento de x de result(result é Page de Product e cada elemento dele é um Product) e instancio 
		//um ProductDTO. Para isso utilizo o construtor em que passo um objeto Product como parâmetro
		//para isso tenho que alterar o Controller que ao invés de receber um ListDTO, vai receber um PageDTO
		Page<ProductMinDTO> pageDto = result.map(x -> new ProductMinDTO(x));
		return pageDto;
	}
	
	//Inserir novo produto no banco de dados (POST)
	@Transactional//devo remover o readyOnly=true, pois a operção é de salvar e não apenas consultar
	//vai receber como argumento um ProductDTO vindo Controller
	//Aqui vou ter que transformar ProductDTO em Product para enviar ao Repository. Ou seja, instancia um novo objeto Product
	//Olha que interessante é o caminho inverso do GET
	//Os valores dos atributos do novo produto vieram forncedidos pelo usuario, no front end quando ele preencheu o formulário
	//Vou ter que passar os atributos do ProductDto recebido para o Product, para isso utilizo o set.
	public ProductDTO insert(ProductDTO dto) {	
		
		//setar os valores no Product que vieram do DTO;
		//Utilizo o método que criei em vez das linhas abaixo. Essas linhas abaixo estão no método
		Product product = new Product();
//		product.setName(dto.getName());		
//		product.setDescription(dto.getDescription());
//		product.setPrice(dto.getPrice());
//		product.setImgUrl(dto.getImgUrl());		
		
		copyDTOToProduct(dto, product);
		
		//salvar no banco de dados. Salvo no banco de dados e obtenho uma nova referência para ele
		//e coloco na variável de mesmo nome. Nessas duas linhas a explicação é porque o professor fez isso,
		//mas acredito que não seja necessário. Apenas deixa mais truncado o código:
		//product = repository.save(product);
		repository.save(product);
		
		//retornar para controller. Vou converte Product para ProductDTO para ser o retorno do meu método
		//Controller quando chamar esse método, vai passar o parâmetro que veio do usuário.
		//Aí esse método transforma ProductDTO em Product e salva no banco de dados
		//Depois converto o ProductDTO em Product e retono esse DTO ao Controller
		return new ProductDTO(product);		
	}
	
	//Update (PUT)
	//Recebe dois parâmetros: id (id do produto que quero atualizar) e dto (é o corpo com as atualizações)
	//Primeiro, devo buscar no banco de dados o Product com aquele id
	//Segundo, substituir os valores dos atributos de Product por ProductDTO
	//Terceiro, salvar no banco de dados
	//Quarto, transformar o Product em ProductDTO e retornar ao Controller
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		////Como não tem o orElseThrow(), como no método getById, preciso fazer aqui um try/catch
		try {
			//Quando eu fiz eu utilizei o findById que vai no banco de dados e faz uma consulta sql
			//Já o professor fez diferente
			//Abaixo como eu fiz
			//Optional<Product> result = repository.findById(id);
			//Product product = result.get(); //Pegar o objeto Product dentro do Optinal
			
			//O professor fez diferente, utilizou o método getReferenceByID em que não faz a consulta no banco de dados
			//Apenas instancia um product pelo id do banco de dados através da JPA 
			//getReferenceByID prepara o objeto product monitorado pela JPA
			Product product = repository.getReferenceById(id);
			
			//setar os valores no Product que vieram do DTO;
			copyDTOToProduct(dto, product);			
			
			//salvar no banco de dados.
			repository.save(product);
			
			//retornar um DTO para controller
			return new ProductDTO(product);
		}
		//se der o erro abaixo (EntityNotFoundException), capturo e lanço a minha exceção customizada que posteriormente é capturada
		//pela class ControllerExceptionHandler e sua annotation @ControllerAdvice
		catch (EntityNotFoundException e){
			throw new ResourceNotFoundException("Recurso não encontrado"); //passo a mensagem que vai estar no atributo "error" no conteúdo apresetnado no postman
				//esse "error" é o atributo da class CustomError
		}
	}
	/*
	//Deletar um produto por id	- sem tratamento com uma exceção personalizada
	@Transactional
	public void delete(Long id) {
		//repository.deleteById(id);//fiz o retorno void ao invés de retornar os dados do produto deletado		
		
		//Product product = repository.getReferenceById(id); //caso eu quisesse retornar os dados do produto deletado 
		//return new ProductDTO(product); 
	}	
	
	*/
	//Deletar um produto por id	- com tratamento com uma exceção personalizada
	//O spring não lança execção se eu tentar deletar um id que não existe, informa que deu certo (204 verde)
	//Não retorno nada
	@Transactional(propagation = Propagation.SUPPORTS)//por causa do erro no h2 JdbcSQLIntegrityConstraintViolationException 
	public void delete(Long id) {
		//testar se no banco de dados existe o id
		if (!repository.existsById(id)) { //se o id não existe, lanço a exceção personalizada
			throw new ResourceNotFoundException("Id que você quer deletar não existe");
		}		 
		
		//tenho que tratar abaixo a exceção DataIntegrityViolationException que ocorre quando tento deletar um produto
		//em que esteja associado a um pedido. Criei outra exceção personalizada, pois não é erro de recurso não encontrado (ResourceNotFoundException)
		try {
	        repository.deleteById(id);    		
		}
	    catch (DataIntegrityViolationException e) {
	        throw new DataBaseException("Falha de integridade referencial");
	   	}
	}
	
	//Método para não ficar toda vez escrevendo as linhas para transformar um DTO em Product
	//private pois só vou utilizar nessa classe
	private void copyDTOToProduct(ProductDTO dto, Product product) {		
		product.setName(dto.getName());		
		product.setDescription(dto.getDescription());
		product.setPrice(dto.getPrice());
		product.setImgUrl(dto.getImgUrl());
		
		product.getCategories().clear();//primeiro limpo minha lista de categorias, pois no caso de uma atualização, excluo todas as categorias e reinsiro novamente
		for (CategoryDTO catDTO : dto.getCategories()) {//Copiar as categorias do DTO para a entity
			Category cat = new Category();
			cat.setId(catDTO.getId());
			product.getCategories().add(cat);
		}
		//return product; //professor colocou para retornar void e funciona também, como também funciona retornando product
		//returnando void não consigo armazenar em uma variável esse novo product setado, tudo fica em memória.
		
	}
	
		
	

}
