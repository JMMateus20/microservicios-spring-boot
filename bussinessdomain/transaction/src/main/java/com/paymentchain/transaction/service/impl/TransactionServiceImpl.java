package com.paymentchain.transaction.service.impl;

import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.paymentchain.transaction.dto.TransactionRequestDTO;
import com.paymentchain.transaction.dto.TransactionResponseDTO;
import com.paymentchain.transaction.dto.microservice.Customer;
import com.paymentchain.transaction.dto.microservice.Product;
import com.paymentchain.transaction.entity.Transaction;
import com.paymentchain.transaction.exceptions.NotFoundException;
import com.paymentchain.transaction.repository.TransactionRepository;
import com.paymentchain.transaction.service.TransactionService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService{
	
	private final TransactionRepository transRep;
	private final WebClient.Builder webClientBuilder;

	@Override
	public Mono<ResponseEntity<?>> insertar(TransactionRequestDTO dto) {
		Mono<Customer> customer=this.getCustomer(dto.getIdCustomer());
		Mono<Product> product=this.getProduct(dto.getIdProduct());
		
		return Mono.zip(customer, product).flatMap(tuple->{
			Customer customerBD=tuple.getT1();
			Product productBD=tuple.getT2();
			Transaction transactionNew=this.transRep.save(new Transaction(dto.getIdCustomer(), dto.getIdProduct()));
			TransactionResponseDTO response=new TransactionResponseDTO(
					transactionNew.getId(), 
					transactionNew.getIdCustomer(), 
					customerBD.getName(), 
					transactionNew.getIdProduct(), 
					productBD.getName());
			return Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(response));
		});
			
		
	}
	

	@Override
	public ResponseEntity<?> listarPorCliente(Long idCliente) {
		return ResponseEntity.ok(this.transRep.listarPorCliente(idCliente));
	}
	
	private Mono<Product> getProduct(Long idProduct) {
		WebClient build=this.createWebClient("http://bussinessdomain-product/products");
		return build.method(HttpMethod.GET).uri("/"+idProduct) 
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response->Mono.error(()->new NotFoundException("Product not found")))
					
				.bodyToMono(Product.class);
		
	}
	
	private Mono<Customer> getCustomer(Long idCustomer) {
		WebClient build=this.createWebClient("http://bussinessdomain-customer/customers");
		return build.method(HttpMethod.GET).uri("/"+idCustomer) 
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response->Mono.error(()->new NotFoundException("Customer not found")))
				.bodyToMono(Customer.class);
				
		
	}
	
	
	private WebClient createWebClient(String baseUrl) {
		return this.webClientBuilder
				.baseUrl(baseUrl) 
				.defaultHeader(HttpHeaders.CONTENT_TYPE, 
						MediaType.APPLICATION_JSON_VALUE)
				.defaultUriVariables(Collections.singletonMap
						("url", baseUrl))
				.build();
	}

}
