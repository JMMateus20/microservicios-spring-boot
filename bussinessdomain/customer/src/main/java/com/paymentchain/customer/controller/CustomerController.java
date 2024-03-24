package com.paymentchain.customer.controller;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.paymentchain.customer.dto.CustomerProductRequestDTO;
import com.paymentchain.customer.dto.CustomerProductResponseDTO;
import com.paymentchain.customer.dto.CustomerProductResponseDTO2;
import com.paymentchain.customer.dto.CustomerRequestDTO;
import com.paymentchain.customer.dto.CustomerResponseDTO;
import com.paymentchain.customer.dto.CustomerResponseDTO2;
import com.paymentchain.customer.dto.TransactionsResponseDTO;
import com.paymentchain.customer.dto.microservice.Transaction;
import com.paymentchain.customer.entity.Customer;
import com.paymentchain.customer.entity.CustomerProduct;
import com.paymentchain.customer.exceptions.NotFoundException;
import com.paymentchain.customer.repository.CustomerProductRepository;
import com.paymentchain.customer.repository.CustomerRepository;
import com.paymentchain.customer.dto.microservice.Producto;

import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@AllArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	
	private final CustomerRepository customerRep;
	private final CustomerProductRepository cpRep;
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	/*HttpClient client=HttpClient.create()
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.option(EpollChannelOption.TCP_KEEPIDLE, 300)
			.option(EpollChannelOption.TCP_KEEPINTVL, 60)
			.responseTimeout(Duration.ofSeconds(1))
			.doOnConnected(connection->{
				connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
				connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
			});*/
	
	@GetMapping
	public Page<CustomerResponseDTO> listAll(@RequestParam(name = "page", defaultValue = "0", required = true) Integer page){
		return this.customerRep.listarTodos(PageRequest.of(page, 2));
	}
	
	@PostMapping
	public ResponseEntity<CustomerResponseDTO> post(@RequestBody CustomerRequestDTO customer){
		Customer customerNew=this.customerRep.save(new Customer(customer.getCode(), 
				customer.getName(), customer.getPhone(), customer.getIban(),
				customer.getSurname(), customer.getAddress()));
		CustomerResponseDTO dto=new CustomerResponseDTO(customerNew.getId(), 
				customerNew.getCode(), customerNew.getName(), customerNew.getPhone(),
				customerNew.getIban(), customerNew.getSurname(), customerNew.getAddress());
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}
	
	@GetMapping("/{idCustomer}")
	public ResponseEntity<CustomerResponseDTO2> get(@PathVariable Long idCustomer){
		Customer customerBD=this.customerRep.findById(idCustomer)
				.orElseThrow(()->new NotFoundException("Customer no encontrado en la base de datos"));
		List<CustomerProductResponseDTO2> productos=customerBD.getProducts().stream()
				.map(prod->new CustomerProductResponseDTO2(prod.getId(), prod.getProductId(),
						this.getProductName(prod.getProductId()))).collect(Collectors.toList());
		return ResponseEntity.ok(new CustomerResponseDTO2(customerBD.getId(), 
				customerBD.getCode(), 
				customerBD.getName(), 
				customerBD.getPhone(), 
				customerBD.getIban(), 
				customerBD.getSurname(), 
				customerBD.getAddress(), 
				productos));
	}
	
	@PutMapping("/{idCustomer}")
	public ResponseEntity<CustomerResponseDTO> put(@PathVariable Long idCustomer, @RequestBody CustomerRequestDTO body){
		Customer customerBD=this.customerRep.findById(idCustomer)
				.orElseThrow(()->new NotFoundException("Customer no encontrado en la base de datos"));
		customerBD.setCode(body.getCode());
		customerBD.setName(body.getName());
		customerBD.setPhone(body.getPhone());
		customerBD.setIban(body.getIban());
		customerBD.setSurname(body.getSurname());
		customerBD.setAddress(body.getAddress());
		this.customerRep.save(customerBD);
		return ResponseEntity.ok(new CustomerResponseDTO(customerBD.getId(), 
				customerBD.getCode(), customerBD.getName(), customerBD.getPhone(), 
				customerBD.getIban(), customerBD.getSurname(), customerBD.getAddress()));
	}
	
	@DeleteMapping("{idCustomer}")
	public ResponseEntity<String> delete(@PathVariable Long idCustomer){
		Customer customerBD=this.customerRep.findById(idCustomer)
				.orElseThrow(()->new NotFoundException("Customer no encontrado en la base de datos"));
		this.customerRep.delete(customerBD);
		return ResponseEntity.ok("Cliente eliminado correctamente");
		
	}
	
	
	
	@PostMapping("/products")
	public ResponseEntity<CustomerProductResponseDTO> insertarProducto(@RequestBody CustomerProductRequestDTO dto){
		Customer customerBD=this.customerRep.findById(dto.getCustomerId()).orElseThrow(()->new NotFoundException("Customer not found"));
		customerBD.getProducts().add(new CustomerProduct(dto.getProductId(), this.getProductName(dto.getProductId()), customerBD));
		this.customerRep.save(customerBD);
		CustomerProduct cpNew=customerBD.getProducts().get(customerBD.getProducts().size()-1);
		cpNew.setProductName(this.getProductName(cpNew.getProductId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(new CustomerProductResponseDTO(cpNew.getId(), cpNew.getProductId(), cpNew.getProductName(), cpNew.getCustomer().getName(), cpNew.getCustomer().getAddress()));
		
		
	}
	
	
	@GetMapping("/products/{customerID}")
	public ResponseEntity<List<CustomerProductResponseDTO>> obtenerProductos(@PathVariable Long customerID){
		Customer customerBD=this.customerRep.findById(customerID).orElseThrow(()->new NotFoundException("Customer not found"));
		List<CustomerProductResponseDTO> productosPorCliente=this.cpRep.listarPorCustomer(customerBD);
		productosPorCliente.forEach(producto->{
			producto.setProductName(this.getProductName(producto.getProductId()));
		});
		return ResponseEntity.ok(productosPorCliente);
	}
	
	@GetMapping("/transactions/{idCliente}")
	public Mono<ResponseEntity<List<TransactionsResponseDTO>>> listarTransacciones(@PathVariable Long idCliente){
		Customer customerBD=this.customerRep.findById(idCliente).orElseThrow(
				() -> new NotFoundException("Customer not found"));
		Mono<List<Transaction>> transacciones=this.getTransactions(customerBD.getId());
		return transacciones.flatMapMany(Flux::fromIterable) //transforma la lista en un Flux<Transaction>, se utiliza para iterar y utilizar cada elemento de la lista por separado.
				.flatMap(t->{
					Mono<Producto> productoMono=this.getProduct(t.getIdProduct()); //para cada transaccion, se encuentra el nombre de su producto obteniendo todo el producto completo
					return productoMono.map(producto->new TransactionsResponseDTO(t.getId(),//se hace un map para manipular el objeto Producto que esta dentro del Mono y realizar alguna accion, es decir, crear la respuesta.
							t.getIdCustomer(), 
							customerBD.getName(), 
							t.getIdProduct(), 
							producto.getName()));
				}).collectList() //Recolecta todos los objetos TransactionsResponseDTO generados en el paso anterior en una lista.
				.map(responseList->ResponseEntity.ok(responseList)) //se hace un map para mostrar la lista como respuesta correcta
				.defaultIfEmpty(ResponseEntity.noContent().build()); //si la lista está vacía, retorna un noContent
		
	}
	
	
	private String getProductName(Long id) {
		WebClient build=this.webClientBuilder
				.baseUrl("http://bussinessdomain-product/products") //es el requestMapping del controlador del microservicio de productos
				.defaultHeader(HttpHeaders.CONTENT_TYPE, 
						MediaType.APPLICATION_JSON_VALUE)
				.defaultUriVariables(Collections.singletonMap
						("url", "http://bussinessdomain-product/products"))
				.build();
		
		JsonNode block=build.method(HttpMethod.GET).uri("/"+id) //como vamos a obtener un producto por su id llamado al
				//microservicio de productos, entonces debemos enviarle el id al microservicio de productos,
				//esto se hace concatenando la url anterior del microservicio de productos un "/" y el id, para que el
				//metodo get del controlador de productos pueda encontrar el producto y retornarlo
				.retrieve().bodyToMono(JsonNode.class).block();
		String name=block.get("name").asText();   //del json de respuesta que nos da el microservicio de productos
				//extraemos la propiedad name que es la que queremos obtener.
		return name;
	}
	
	private Mono<Producto> getProduct(Long id) {
		WebClient build=this.webClientBuilder
				.baseUrl("http://bussinessdomain-product/products") //es el requestMapping del controlador del microservicio de productos
				.defaultHeader(HttpHeaders.CONTENT_TYPE, 
						MediaType.APPLICATION_JSON_VALUE)
				.defaultUriVariables(Collections.singletonMap
						("url", "http://bussinessdomain-product/products"))
				.build();
		return build.method(HttpMethod.GET)
				.uri("/"+id)
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response->Mono.error(()->new NotFoundException("Producto no encontrado")))
				.bodyToMono(Producto.class);
	}
	
	private Mono<List<Transaction>> getTransactions(Long idCliente){
		WebClient build=this.webClientBuilder
				.baseUrl("http://bussinessdomain-transaction/transactions") 
				.defaultHeader(HttpHeaders.CONTENT_TYPE, 
						MediaType.APPLICATION_JSON_VALUE)
				.defaultUriVariables(Collections.singletonMap
						("url", "http://bussinessdomain-transaction/transactions"))
				.build();
		return build.method(HttpMethod.GET)
		.uri("/bycustomer/"+idCliente)
		.retrieve().bodyToFlux(Transaction.class)
		.collectList();
	}

}
