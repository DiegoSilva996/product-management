package com.nttdata.createProduct.repository;

import com.nttdata.createProduct.entity.Product;
import com.nttdata.createProduct.entity.eWallet;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String>{
	//Mono <eWallet> saveWallet (eWallet wallet);
	Flux<Product> findByClientId(String clientId);
    Flux<Product> findByProductTypeAndStatus(String ProductType, String Status);
    Flux<Product> findByProductTypeAndClientId (String ProductType, String clientId);    
}
