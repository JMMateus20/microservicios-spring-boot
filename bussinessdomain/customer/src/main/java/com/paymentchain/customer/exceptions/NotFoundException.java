package com.paymentchain.customer.exceptions;

public class NotFoundException extends RuntimeException{
	
	public NotFoundException(String mensaje) {
		super(mensaje);
	}

}
