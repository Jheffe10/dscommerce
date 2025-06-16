package com.devsuperior.dscommerce.entities;

import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_payment")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant moment;
	
	@OneToOne
	@MapsId //Diz que o id do Payment é o mesmo id do Order. O Payment não tem um id próprio. Ele herda o id do Order
	//com isso vai relacionar o mesmo id do pedido com o id do pagamento. Ou seja, se o pedido é id 5, o 
	//payment também vai ter id 5. assim na tabela payment vai ter apenas uma coluna (order_id) que vai 
	//representar tanto o id de payment(chave primária) quanto o order_id (chave estrangeira)
	// anotação @MapsId é usada no JPA para mapear uma chave estrangeira (foreign key) que também funciona como chave 
	//primária (primary key) da entidade
	//o id do Payment é o mesmo id do Order
	//Ou seja, a chave primária (id) e a chave estrangeira (order_id) são a mesma coluna no banco.
	//isso é utilizados em relacionamentos um para um
	private Order order;

	public Payment() {		
	}
	
	public Payment(Long id, Instant moment, Order order) {
		super();
		this.id = id;
		this.moment = moment;
		this.order = order;
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

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
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
		Payment other = (Payment) obj;
		return Objects.equals(id, other.id);
	}
	
	
	
	
	
}
