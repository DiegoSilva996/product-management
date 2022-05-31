package com.nttdata.createProduct.controllers;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.nttdata.createProduct.controller.ProductController;
import com.nttdata.createProduct.entity.Product;
import com.nttdata.createProduct.service.ProductService;

import reactor.core.publisher.Mono;


public class ProductControllerTest {
	
	@MockBean
	private ProductService productService;
		
	@Autowired
	private WebTestClient webTestClient;
	
	@Test
	void createProductTest() {
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
		when(productService.createProduct(producttoTest)).thenReturn(Mono.just(producttoTest));
		
		/*webTestClient.post().uri("/products/createProduct")
			.body(Mono.just(producttoTest), Product.class )
			.exchange()
			.expectStatus().isOk();
		*/
	}

}
