package com.paymentchain.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.paymentchain.customer.dto.CustomerProductResponseDTO;
import com.paymentchain.customer.entity.Customer;
import com.paymentchain.customer.entity.CustomerProduct;

public interface CustomerProductRepository extends JpaRepository<CustomerProduct, Long>{
	
	
	@Query("SELECT new com.paymentchain.customer.dto.CustomerProductResponseDTO(c.id, c.productId, c.customer.name, c.customer.address) " + 
	"FROM CustomerProduct c WHERE c.customer=:customer")
	List<CustomerProductResponseDTO> listarPorCustomer(@Param("customer") Customer customer);

}
