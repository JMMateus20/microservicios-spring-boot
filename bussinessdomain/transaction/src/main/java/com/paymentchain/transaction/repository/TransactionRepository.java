package com.paymentchain.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.paymentchain.transaction.dto.TransactionResponseDTO;
import com.paymentchain.transaction.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	
	
	@Query("SELECT t FROM Transaction t WHERE t.idCustomer=:idCliente")
	List<Transaction> listarPorCliente(@Param("idCliente") Long idCliente);

}
