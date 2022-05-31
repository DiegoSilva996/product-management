package com.nttdata.createProduct.service.Impl;


import com.nttdata.createProduct.entity.Customer;
import com.nttdata.createProduct.repository.CustomerRepository;
import com.nttdata.createProduct.service.CustomerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service
public class CustomerServiceImpl implements CustomerService{
    
	@Autowired
	private CustomerRepository customerRepository;

    
	public Flux<Customer> getAll() {
		return customerRepository.findAll();
	}

	public Mono<Customer> getCustomerData(String id) {
		return customerRepository.findById(id);
	}
	
	public Mono<Customer> createCustomer(Customer new_customer) {
		new_customer.setStatus("ACTIVE");
		return customerRepository.save(new_customer);
	}
	

	public Mono<Customer> updateCustomer(Customer customer,String id){
		return customerRepository.findById(id)
				.map(c->{
					c.setAddress(customer.getAddress());
					c.setClientType(customer.getClientType());
					c.setEmail(customer.getEmail());
					c.setId(id);
					c.setLastName(customer.getLastName());
					c.setName(customer.getName());
					c.setRUC(customer.getRUC());
					c.setStatus("ACTIVE");
					return c;
				}).flatMap(customerRepository::save);
	}
	
	public Mono<Void> deleteCustomer(String id){
		return customerRepository.deleteById(id);
	}
	
	public Mono<Customer> setInactiveCustomer(String id){
		return customerRepository.findById(id)
				.doOnNext(e->e.setStatus("INACTIVE"))
				.flatMap(customerRepository::save);
	}



}
