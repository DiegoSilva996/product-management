package com.nttdata.createProduct.service;



import org.springframework.stereotype.Service;

import com.nttdata.createProduct.entity.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ClientService {
	
    Flux<Client> getAll();
    Mono<Client> createClient(Client new_client);
}
