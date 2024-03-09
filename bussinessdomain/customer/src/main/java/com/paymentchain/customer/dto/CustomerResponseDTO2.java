package com.paymentchain.customer.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO2 {
	
	private Long id;
	private String code;
	private String name;
	private String phone;
	private String iban;
	private String surname;
	private String address;
	private List<CustomerProductResponseDTO2> productos=new ArrayList<>();

}
