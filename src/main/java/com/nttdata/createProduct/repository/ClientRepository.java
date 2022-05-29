package com.nttdata.createProduct.repository;

import com.nttdata.createProduct.entity.Client;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository  extends ReactiveMongoRepository<Client, String> {
    
}
