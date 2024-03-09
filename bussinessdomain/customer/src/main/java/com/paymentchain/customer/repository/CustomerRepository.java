package com.paymentchain.customer.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.paymentchain.customer.dto.CustomerProductResponseDTO;
import com.paymentchain.customer.dto.CustomerResponseDTO;
import com.paymentchain.customer.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	@Query("SELECT new com.paymentchain.customer.dto.CustomerResponseDTO(c.id, c.code, c.name, c.phone, c.iban, c.surname, c.address) " + 
	" FROM Customer c ORDER BY c.name ASC")
	Page<CustomerResponseDTO> listarTodos(Pageable pageable);
	
	
	@Query("SELECT new com.paymentchain.customer.dto.CustomerProductResponseDTO(c.id, c.productId, c.customer.name, c.customer.address) " + 
	" FROM CustomerProduct c WHERE c.customer.id=:idCustomer ORDER BY c.customer.name ASC")
	List<CustomerProductResponseDTO> listarProductos(@Param("idCustomer") Long idCustomer);

}
