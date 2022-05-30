package com.nttdata.createProduct.controller;


import com.nttdata.createProduct.entity.Customer;
import com.nttdata.createProduct.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/client")
public class CustomerController {
    
	@Autowired
    private CustomerService clientService;


    
    //CRUD
    @GetMapping(value = "/all")
    @TimeLimiter(name="createTime")
    @CircuitBreaker(name="createCircuit")
    public Flux<Customer> getAll() {
        log.info("lista todos");
        return clientService.getAll();
    }  

    @GetMapping("getClient/{id}")
    @ResponseBody
    @TimeLimiter(name="consultTime")
    @CircuitBreaker(name="consultCircuit")
    public Mono<Customer> getClientData(@PathVariable("id") String id){
      
    	return clientService.getCustomerData(id);
     /* if (client_doc.isPresent()) {
        salida.put("client", client_doc);
      }else{
        salida.put("status", "Id de Cliente no encontrado");
      }
      return ResponseEntity.ok(salida);*/
    }


    
    @PostMapping(value = "/create")
    @TimeLimiter(name="createTime")
    @CircuitBreaker(name="createCircuit")
    public Mono<Customer> createClient(@RequestBody Customer new_client){
       
        return clientService.createCustomer(new_client);
    }

    
    @PutMapping("/update/{id}")
    @TimeLimiter(name="createTime")
    @CircuitBreaker(name="createCircuit")
    public Mono<Customer> updateClient(@PathVariable("id") String id, @RequestBody Customer temp) {
    	return clientService.updateClient(temp, id);
     /* if (client.isPresent()) {
        temp.setId(id);
        return new ResponseEntity<>(clientService.createClient(temp), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }*/
    }
    
    
    @PutMapping("setInactive/{id}")
    @TimeLimiter(name="createTime")
    @CircuitBreaker(name="createCircuit")
    public Mono<Customer> setInactive(@PathVariable("id") String id/*, @RequestBody Client temp_client*/) {
      return clientService.setInactiveCustomer(id);
      /*if (client_doc.isPresent()) {
        Client _client = client_doc.get();
        _client.setStatus("INACTIVE");
        return new ResponseEntity<>(clientRepo.save(_client), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }*/
    } 
    
    /*
    public ResponseEntity<String> customerContactInfoFallback(Exception e) {
      return new ResponseEntity<String>("GET: Client contact info endpoint is not available right now.", HttpStatus.OK);
    }*/

}