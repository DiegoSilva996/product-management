package com.nttdata.createProduct.service.Impl;


import com.nttdata.createProduct.entity.Client;
import com.nttdata.createProduct.repository.ClientRepository;
import com.nttdata.createProduct.service.ClientService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service
public class ClientServiceImpl implements ClientService{
    
	@Autowired
	private ClientRepository clientRepository;

    
	public Flux<Client> getAll() {
		return clientRepository.findAll();
	}

	public Mono<Client> getClientData(String id) {
		return clientRepository.findById(id);
	}
	
	public Mono<Client> createClient(Client new_client) {
		new_client.setStatus("ACTIVE");
		return clientRepository.save(new_client);
	}
	

	public Mono<Client> updateClient(Client client,String id){
		return clientRepository.findById(id)
				.map(c->{
					c.setAddress(client.getAddress());
					c.setClientType(client.getClientType());
					c.setEmail(client.getEmail());
					c.setId(id);
					c.setLastName(client.getLastName());
					c.setName(client.getName());
					c.setRUC(client.getRUC());
					c.setStatus("ACTIVE");
					return c;
				}).flatMap(clientRepository::save);
	}
	
	public Mono<Void> deleteClient(String id){
		return clientRepository.deleteById(id);
	}
	
	public Mono<Client> setInactiveClient(String id){
		return clientRepository.findById(id)
				.doOnNext(e->e.setStatus("INACTIVE"))
				.flatMap(clientRepository::save);
	}
	

}
