package com.nttdata.createProduct.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nttdata.createProduct.entity.Client;

@Repository
public interface ClientRepository extends ReactiveMongoRepository<Client, String>{

}
