package com.nttdata.createProduct.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.createProduct.Util.Mapper;
import com.nttdata.createProduct.entity.Product;
import com.nttdata.createProduct.entity.Transaction;
import com.nttdata.createProduct.repository.ProductRepository;
import com.nttdata.createProduct.repository.TransactionRepository;
import com.nttdata.createProduct.service.CustomerService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
//import redis.clients.jedis.Transaction;


@Component
@Slf4j
public class KafkaConsumer {
	
	@Autowired
    private ProductRepository productRepository;
	@Autowired
	private TransactionRepository transactionRepository;


	  //Funciones con programación reactiva para actualizar montos de bootcoin
	  public Mono<String> saveAmount(String id, Double amountBootCoin, Double amountMoney) {
		return productRepository.findById(id)
				.map(pro -> transform(pro, amountBootCoin,amountMoney ))   
				.flatMap(productRepository::save)          
				.map(Product::getId);                       
	  }
	  private Product transform(Product toBeSaved, Double amountBootCoin, Double amountMoney) {
		  Double newAmountBoot = toBeSaved.getAmountBootCoin() + amountBootCoin;
		  Double newAmountMoney = toBeSaved.getAmount() + amountMoney;
		  toBeSaved.setAmountBootCoin(newAmountBoot);
		  toBeSaved.setAmount(newAmountMoney);
		  return toBeSaved;
	  }
	

	  
	
	@KafkaListener(topics="${kafka.subscribed-topic.name}")
	public void consumeEvent(String message) throws JsonProcessingException, InterruptedException{

		String[] data = message.split("%%");
		int  purchaseRate = Integer.parseInt(data[1]); 
		int sellingRate = Integer.parseInt(data[2]);
		Transaction trans = Mapper.OBJECT_MAPPER.readValue(message, Transaction.class);

		//Actualizar monederos
		//Monedero origen (quien vende)
		Double newAmountMoney = trans.getAmount()* sellingRate; 
		saveAmount(trans.getIdProduct(), - trans.getAmount(), newAmountMoney);

		//Monedero destino (quien compra)
		Double newAmountMoneyDestination = trans.getAmount()* purchaseRate * -1; 
		saveAmount(trans.getIdProduct(),  trans.getAmount(), newAmountMoneyDestination);		

		//Cambiar estado de transaccion 
		trans.setOperationStatus("SUCCESFUL_TRANSACTION");
		transactionRepository.save(trans);

		log.info("Transacción actualizada, productos actualziados, idTransaction -> " + trans.getId());
		
	}
}
