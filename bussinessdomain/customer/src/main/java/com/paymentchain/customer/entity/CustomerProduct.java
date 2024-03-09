package com.paymentchain.customer.entity;

import org.hibernate.annotations.WhereJoinTable;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class CustomerProduct {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long productId;
	@Transient
	private String productName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="customer_id", nullable = false)
	private Customer customer;

	public CustomerProduct(Long productId, String productName, Customer customer) {
		
		this.productId = productId;
		this.productName = productName;
		this.customer = customer;
	}

	public CustomerProduct(Long productId, Customer customer) {
		this.productId = productId;
		this.customer = customer;
	}
	
	
	
	

}
