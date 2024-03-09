package com.paymentchain.customer.dto.microservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
	
	private Long id;
	private String name;
	private String code;

}
