package com.paymentchain.customer.dto;

import com.paymentchain.customer.entity.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProductRequestDTO {
	
	
	private Long productId;
	
	private Long customerId;
	
	

}
