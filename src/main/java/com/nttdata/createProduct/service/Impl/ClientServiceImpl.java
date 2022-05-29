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

    @Override
	public Flux<Client> getAll() {
		return clientRepository.findAll();
	}

	@Override
	public Mono<Client> createClient(Client new_client) {
		return clientRepository.save(new_client);
	}



}
