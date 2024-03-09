package com.paymentchain.customer.dto.microservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
	
	private Long id;
	private Long idCustomer;
	private Long idProduct;
	

}
