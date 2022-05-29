package com.nttdata.createProduct.repository;

import java.util.List;

import com.nttdata.createProduct.entity.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String>{
	
    List<Product> findByClientId(String clientId);
    List<Product> findByProductTypeAndStatus(String ProductType, String Status);
    List<Product> findByProductTypeAndClientId (String ProductType, String clientId);    
}
