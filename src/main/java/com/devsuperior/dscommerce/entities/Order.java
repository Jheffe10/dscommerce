package com.devsuperior.dscommerce.entities;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_order")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//annotation abaixo serve para salvar no banco de dados sem o horário UTC, salva apenas o horário
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	//aqui acredito que o professor tenha se equivocado, ele disse que a annotation acima salva no horário UTC, na verdade 
	//vai salvar no horário local e não converte para UTC. Para slavar no UTC teria que colocar a annotation:
	//@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE") que aí converte para o UTC no banco de dados
	private Instant moment;
	private OrderStatus status;
	
	@ManyToOne
	@JoinColumn(name = "client_id") //para mapear uma chave estrangeira (foreign key), dou o nome que quiser a essa coluna
			//Especifica qual coluna da tabela atual será usada para fazer o join (junção) com a tabela da entidade relacionada.
			//A coluna client_id na tabela atual será a foreign key que referencia a chave primária da tabela client (clase User).
	private User client;

	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)//"order" é exatemente o nome do atributo do outro lado da associação (class Payment)
	private Payment payment;
	
	@OneToMany(mappedBy = "id.order")
	private Set<OrderItem> items = new HashSet<>();
	
	public Order() {		
	}
	
	public Order(Long id, Instant moment, OrderStatus status, User client, Payment payment) {
		super();
		this.id = id;
		this.moment = moment;
		this.status = status;
		this.client = client;
		this.payment = payment;
	}	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getMoment() {
		return moment;
	}

	public void setMoment(Instant moment) {
		this.moment = moment;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public User getClient() {
		return client;
	}

	public void setClient(User client) {
		this.client = client;
	}
	
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	
	//Lembrando que para coleções só coloco o get e não set
	//A partir do Order acesso os items
	public Set<OrderItem> getItems() {
		return items;
	}
	
	//Dar um getProduct. A partir do Order acesso os products
	//Peguei o set de items, que é um atributo de order, fiz um stream map (transformo para map), 
	//onde pego os Product e transformo para List. Assim tenho uma lista de product e não mais items
	//Pega uma coleção chamada items (ex.: uma lista, um conjunto, etc.).	
	//Percorre todos os elementos dessa coleção usando Stream API.
	//Para cada elemento, executa o método getProduct(). Pega os Product através de OrderItemPK
	//Coleta o resultado em uma nova lista contendo todos os objetos Product retornados por getProduct().
	public List<Product> getProducts() {
		return items.stream().map(x -> x.getProduct()).toList();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(id, other.id);
	}
	
	

}
