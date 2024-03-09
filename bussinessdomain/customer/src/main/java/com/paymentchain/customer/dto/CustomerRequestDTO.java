package com.paymentchain.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDTO {
	
	
	private String code;
	private String name;
	private String phone;
	private String iban;
	private String surname;
	private String address;

}
