package com.paymentchain.transaction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="transactions")
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long idCustomer;
	private Long idProduct;
	public Transaction(Long idCustomer, Long idProduct) {
		this.idCustomer = idCustomer;
		this.idProduct = idProduct;
	}
	
	

}
