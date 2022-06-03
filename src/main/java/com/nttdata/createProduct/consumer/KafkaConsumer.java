package com.nttdata.createProduct.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.createProduct.Util.Mapper;
import com.nttdata.createProduct.entity.Customer;
import com.nttdata.createProduct.service.CustomerService;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class KafkaConsumer {
	
	@Autowired
	private CustomerService customerService;
	
	@KafkaListener(topics="${kafka.subscribed-topic.name}")
	public void consumeEvent(String message) throws JsonProcessingException, InterruptedException{
		Customer customer = Mapper.OBJECT_MAPPER.readValue(message, Customer.class);
		
		log.info("Mensaje recibido {}"+message);
		//customerService.processTransaction(customer);
	}
}
