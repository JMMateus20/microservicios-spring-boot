package com.paymentchain.product.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paymentchain.product.dto.ProductRequestDTO;
import com.paymentchain.product.exception.NotFoundException;
import com.paymentchain.product.models.Product;
import com.paymentchain.product.repository.ProductRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
	
	
	private final ProductRepository productRep;
	
	
	@PostMapping
	public ResponseEntity<Product> post(@RequestBody ProductRequestDTO product){
		return ResponseEntity.status(HttpStatus.CREATED).body(this.productRep.save(new Product(product.getName(), product.getCode())));
	}
	
	@GetMapping
	public Page<Product> listAll(@RequestParam(name = "page", defaultValue = "0", required = true) Integer page){
		return this.productRep.findAll(PageRequest.of(page, 2));
	}
	
	@GetMapping("/{idProduct}")
	public ResponseEntity<Product> get(@PathVariable Long idProduct){
		Product productBD=this.productRep.findById(idProduct).orElseThrow(
				()->new NotFoundException("Producto no encontrado en la base de datos"));
		return ResponseEntity.ok(productBD);
		
	}
	
	@PutMapping("/{idProduct}")
	public ResponseEntity<Product> put(@PathVariable Long idProduct, @RequestBody ProductRequestDTO product){
		Product productBD=this.productRep.findById(idProduct).orElseThrow(
				()->new NotFoundException("Producto no encontrado en la base de datos"));
		productBD.setName(product.getName());
		productBD.setCode(product.getCode());
		return ResponseEntity.ok(this.productRep.save(productBD));
	}
	
	@DeleteMapping("/{idProduct}")
	public ResponseEntity<String> delete(@PathVariable Long idProduct){
		Product productBD=this.productRep.findById(idProduct).orElseThrow(
				()->new NotFoundException("Producto no encontrado en la base de datos"));
		this.productRep.delete(productBD);
		return ResponseEntity.ok("producto eliminado");
		
	}
	
	

}
