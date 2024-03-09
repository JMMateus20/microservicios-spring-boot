package com.paymentchain.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProductResponseDTO2 {
	
	private Long id;
	private Long productId;
	private String productName;

}
