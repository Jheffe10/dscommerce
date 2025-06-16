package com.devsuperior.dscommerce.entities;

import java.util.Objects;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_order_item")
public class OrderItem {
	
	@EmbeddedId
	private OrderItemPK id = new OrderItemPK();//já instanciar
	
	private Integer quantity;
	private Double price;

	public OrderItem() {
	}
	
    //Na hora de instanciar não passo o OrdferItemPk e sim os id das duas classes relacionadas
	public OrderItem(Order order, Product product, Integer quantity, Double price) {
		id.setOrder(order); //setar o id do OrderItemPK
		id.setProduct(product);
		this.quantity = quantity;
		this.price = price;
	}

	//pega o objeto Order
	public Order getOrder() {
		return id.getOrder();
	}	
	
	public void setOrder(Order order) {
		id.setOrder(order);
	}
	
	//pega o objeto Product
	public Product getProduct() {
		return id.getProduct();
	}
	
	public void setProduct(Product product) {
		id.setProduct(product);
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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
		OrderItem other = (OrderItem) obj;
		return Objects.equals(id, other.id);
	}
	
	
	
	
}
