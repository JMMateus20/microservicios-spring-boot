package com.paymentchain.transaction.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paymentchain.transaction.dto.TransactionRequestDTO;
import com.paymentchain.transaction.service.TransactionService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionController {
	
	
	private final TransactionService tranService;
	
	@PostMapping
	public Mono<ResponseEntity<?>> insertar(@RequestBody TransactionRequestDTO dto){
		return this.tranService.insertar(dto);
	}
	
	@GetMapping("/bycustomer/{idCliente}")
	public ResponseEntity<?> listarPorCliente(@PathVariable Long idCliente){
		return this.tranService.listarPorCliente(idCliente);
	}

}
