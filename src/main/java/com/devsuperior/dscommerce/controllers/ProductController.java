package com.devsuperior.dscommerce.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/products") // recurso products sendo informado da sua rota
public class ProductController {

	@Autowired
	private ProductService service;

	/*
	 * //nessa rota se eu chamar o método GET executa o método abaixo //passo o id
	 * na requisição e ele busca o product com aquele id // a rota fica assim, com
	 * id 2 por exemplo "/products/2" //devo também colocar o @PathVariable
	 * 
	 * @GetMapping(value = "/{id}" ) 
	 * public ProductDTO findById(@PathVariable Long id) { 
	 * 		ProductDTO dto = service.findByID(id); //chamo o método service do ProductDTO passando o id que veio na requisição //e armazeno em uma variável do tipo dto 
	 * 		return dto; //vai retornar o dto }
	 * 
	 * 
	 * //Abaixo vou buscar todos os produtos, sem paginar
	 * 
	 * @GetMapping(value = "/naoPaginada") 
	 * public List<ProductDTO> findAllNaoPaginada(){ 
	 * 		List<ProductDTO> listDto = service.findAllNaoPaginada(); 
	 * 		return listDTO 		
	 * }
	 * //chamo o método service e armazeno em uma variável do tipo listDto return listDto;
	 * //Olha que interessante baste eu repassar a lista de DTO aqui no controller que ele transforma em JSON
	 * //automaticamente e envia para o frontend. No frontend vai ter que trabalhar com o css e html. 
	 * //Sempre devo return um DTO aqui no controller, seja objeto DTO ou ListDTO
	 * 
	 * 
	 * //busca paginada dos produtos. Basta colocar o Pageable como argumento do método e passar ele para o service 
	 * //O Service vai enviar um Page<ProductDTO>, ou seja um objeto página, e não List<ProductDTO> como acima
	 * 
	 * @GetMapping 
	 * public Page<ProductDTO> findAll(@PageableDefault(size = 12) Pageable pageable){ 
	 * 		Page<ProductDTO> listDto = service.findAll(pageable);
	 * 		return listDto;
	 * }
	 * 	//ao colocar @PageableDefault(size = 12) //estou alterando o padrão de 20 para 12 elementos por página Page<ProductDTO>
	 *  //chamo o método service e armazeno em uma variável do tipo listDto
	 *  //Olha que interessante baste eu repassar a lista de DTO aqui no controller que ele transforma 
	 *  // em JSON automaticamente e envia para o frontend. No front end vai ter que trabalhar com o css e html. 
	 *  //Sempre devo return um DTO aqui no controller, seja objeto DTO ou listDTO.
	 * 
	 * 
	 * //inserir um produto
	 * 
	 * @PostMapping // o prâmetro abaixo é o corpo da requisição, é o JSON enviado pelo usuário. 
	 * 				//para isso coloco a annotation @RequestBody 
	 * public ProductDTO insert(@RequestBody ProductDTO dto){ 
	 * 		dto = service.insert(dto);  //chamo o método service e armazeno em uma variável do tipo dto. 
	 * 									//Utilizo a mesma variável para gravar a referência 
	 * 									//assim não preciso declarar o tipo dela:
	 * 									//ProductDTO dto2 = service.insert(dto) //return dto; 
	 * 									//Depois de executar o método do service, devolvo a requisição ao usuário com o DTO 
	 * 
	 * 		return dto;
	 * 									//return service.insert(dto); Poderia ter feito isso,mais fácil, 
	 * 									//para economizar uma linha e ficar mais claro
	 * 		 
	 * }
	 *

	// Métodos retornando um objeto ResponseEntity pra dar o código correto na
	// resposta à requisição
	// Tem muita informação acima nos comentários dos métodos
	/*
	 * @GetMapping(value = "/{id}" ) 
	 * public ResponseEntity<ProductDTO> findById(@PathVariable Long id) { 
	 * 		ProductDTO dto = service.findByID(id);
	 * 						//return dto; Não retorna mais isso:
	 * 		return ResponseEntity.ok(dto);//Vou retornar ao usuário o código 200 (que é o ok), onde o corpo vai ser o DTO 
	 * 
	 * }
	 *  
	/*
	// Customizar com uma exceção - sem o @ControllerAdvice
	// O service lançou a exceção ResourceNotFoundException e capturo aqui e dou esse tratamento abaixo
	// Não quebra a plicação
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		try {
			ProductDTO dto = service.findByID(id);			
			return ResponseEntity.ok(dto);
		} catch (ResourceNotFoundException e) { //vai tratar a exceção ResourceNotFoundException que deu lá no service
			// instanciar a class CustomError para forncer os atributos do erro que aparece
			// para o usuário no postman
			CustomError err = new CustomError(Instant.now(), 404, e.getMessage(), "caminho");
			// e.getMessage pega a mensagem da minha exceção personalizada
			// o path coloquei apenas caminho, mas tem um modo de pegar direto da requisição
			return ResponseEntity.status(400).body(err); // retorno para o front end esse err
		}
	}
	
	*/	
	// Customizar com uma exceção - com o @ControllerAdvice - evita de colocar try catch em todo o código
	// Simplifica o código do método aqui
	// class class ControllerExceptionHandler tem o @ControllerAdvice
	// Não quebra a plicação
	//Apenas isso, com a class ControllerExceptionHandler e sua annotation @ControllerAdvice vai pegar em qualquer lugar do código
	//uma exeção ResourceNotFoundException e tratar, inclusive nos métodos abaixo que fazem busca por id.
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
		ProductDTO dto = service.findByID(id);		
		return ResponseEntity.ok(dto);
	}

	@GetMapping(value = "/naoPaginada")
	public ResponseEntity<List<ProductDTO>> findAllNaoPaginada() {
		List<ProductDTO> listDto = service.findAllNaoPaginada();
		return ResponseEntity.ok(listDto);
	}
	
	@GetMapping //parâmetro de consulta name - é opcional, se não for passado na consulta adota o padrão ""
	public ResponseEntity<Page<ProductMinDTO>> findAll(@RequestParam(name = "name", defaultValue="") String name, @PageableDefault(size = 12) Pageable pageable) {
		Page<ProductMinDTO> listDto = service.findAll(name, pageable);
		return ResponseEntity.ok(listDto);
	}

	// Inserir produto
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto) { //@Valid para fazer as verificações constantes no ProductDTO
		dto = service.insert(dto);
		// Quero repassar o código 201 referente à criação de um novo elemento.
		// para isso chamo o .created que recebe como parâmetro uma URI location
		// Devo isntanciar um objeto URI
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto); // vai retorna o código 201 Created, o corpo dto e o objeto URI
		// Esse objeto URI vai retornar um link para o recurso criado. No postman clico
		// na aba Headers (cabeçalho)
		// lá vai ter a rota do novo recurso criado - http://localhost:8080/products/26.
		// Posso jogar esse caminho na busca por id no postman, vai retornar esse recurso criado.
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	// Update
	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) { // @Valid deve vir na frente do dto
		// @PathVariable é o que vem na rota da requsição e @RequestBody é o que vem no corpo da requisição
		dto = service.update(id, dto);
		return ResponseEntity.ok(dto); // vai retorna o código 200 OK, o corpo dto
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	// Delete
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) { // @PathVariable é o que vem na rota da requisição
		// ResponseEntity<Void> - Resposta com o corpo vazio
		service.delete(id);
		return ResponseEntity.noContent().build(); // vai retorna o código 204 OK, o corpo dto
	}

}
