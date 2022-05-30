package com.nttdata.createProduct.repository;

import com.nttdata.createProduct.entity.Product;

import reactor.core.publisher.Flux;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String>{
	
	Flux<Product> findByClientId(String clientId);
    Flux<Product> findByProductTypeAndStatus(String ProductType, String Status);
    Flux<Product> findByProductTypeAndClientId (String ProductType, String clientId);    
}
