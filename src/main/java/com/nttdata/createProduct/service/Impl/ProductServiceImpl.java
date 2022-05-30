package com.nttdata.createProduct.service.Impl;



import com.nttdata.createProduct.entity.Product;
import com.nttdata.createProduct.repository.ProductRepository;
import com.nttdata.createProduct.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{
    
	@Autowired
    private ProductRepository productRepository;
	
    @Override
    public Flux<Product> getAll() {
      return productRepository.findAll();
    }

    @Override
    public Mono<Product> createProduct(Product product){
      product.setStatus("ACTIVE");
      return productRepository.save(product);
    }
    
    public Mono<Product> getProductData(String id) {
		return productRepository.findById(id);
	}
    
	public Mono<Product> updateProduct(Product product,String id){
		
	
		return productRepository.findById(id)
				.doOnNext(e->e.setId(id))
				.flatMap(productRepository::save);
	}
	
	public Mono<Void> deleteProduct(String id){
		return productRepository.deleteById(id);
	}
	
	public Mono<Product> setInactiveProduct(String id){
		return productRepository.findById(id)
				.doOnNext(e->e.setStatus("INACTIVE"))
				.flatMap(productRepository::save);
	}
    
}
