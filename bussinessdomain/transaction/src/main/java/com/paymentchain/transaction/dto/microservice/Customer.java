package com.paymentchain.transaction.dto.microservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
	
	private Long id;
	private String code;
	private String name;
	private String phone;
	private String iban;
	private String surname;
	private String address;

}
