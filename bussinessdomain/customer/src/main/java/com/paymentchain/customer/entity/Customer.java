package com.paymentchain.customer.entity;



import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class Customer {

	@JsonProperty(access = Access.READ_ONLY)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String code;
	private String name;
	private String phone;
	private String iban;
	private String surname;
	private String address;
	
	@JsonProperty(access = Access.READ_ONLY)
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CustomerProduct> products=new ArrayList<>();

	public Customer(String code, String name, String phone, String iban, String surname, String address) {
		
		this.code = code;
		this.name = name;
		this.phone = phone;
		this.iban = iban;
		this.surname = surname;
		this.address = address;
	}
	
	
}
