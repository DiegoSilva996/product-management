package com.nttdata.createProduct.service;




import org.springframework.stereotype.Service;
import com.nttdata.createProduct.entity.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface ClientService {
	
    Flux<Client> getAll();
    Mono<Client> createClient(Client new_client);
    Mono<Client> getClientData(String id);
    Mono<Client> updateClient(Client client,String id);
    Mono<Void> deleteClient(String id);
    Mono<Client> setInactiveClient(String id);
}
