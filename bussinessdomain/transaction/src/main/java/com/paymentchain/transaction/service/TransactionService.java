package com.paymentchain.transaction.service;

import org.springframework.http.ResponseEntity;

import com.paymentchain.transaction.dto.TransactionRequestDTO;

import reactor.core.publisher.Mono;

public interface TransactionService {
	
	
	Mono<ResponseEntity<?>> insertar(TransactionRequestDTO dto);
	
	ResponseEntity<?> listarPorCliente(Long idCliente);
	
	

}
