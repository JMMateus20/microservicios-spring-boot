package com.paymentchain.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsResponseDTO {
	
	
	private Long idTransaction;
	private Long idCustomer;
	private String customerName;
	private Long idProduct;
	private String productName;

}
