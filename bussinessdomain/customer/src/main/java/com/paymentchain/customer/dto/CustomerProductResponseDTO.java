package com.paymentchain.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProductResponseDTO {
	
	private Long id;
	private Long productId;
	private String productName;
	private String customerName;
	private String customerAddress;
	public CustomerProductResponseDTO(Long id, Long productId, String customerName, String customerAddress) {
		this.id = id;
		this.productId = productId;
		this.customerName = customerName;
		this.customerAddress = customerAddress;
	}
	
	
	
	
	
	
	
	

}
