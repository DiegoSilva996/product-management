package com.nttdata.createProduct.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nttdata.createProduct.entity.Product;
import com.nttdata.createProduct.repository.ProductRepository;
import com.nttdata.createProduct.service.Impl.ProductServiceImpl;

import reactor.core.publisher.Mono;

public class CreateProductServiceTest {

	@Mock
	private ProductRepository productRepository;
	
	@InjectMocks
	private ProductServiceImpl productService;
	
	private Product producttoTest;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		Product producttoTest=Product.builder()
				.clientId("123")
				.creationDate(null)
				.transactionDate(null)
				.maximumTransactionLimit(3)
				.numberOfFreeTransactions(1)
				.maintenanceCommission(5.00)
				.amount(99.9)
				.productType("BUSSINESS")
				.status("ACTIVE")
				.owners(null)
				.authorizedSigner(null)
				.build();
	}
	
	@Test
	void create() {
		when(productRepository.save(producttoTest)).thenReturn(Mono.just(producttoTest));
		assertNotNull(productService.createProduct(producttoTest));
		
	}

	
}
