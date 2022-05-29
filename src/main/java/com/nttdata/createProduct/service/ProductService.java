package com.nttdata.createProduct.service;



import com.nttdata.createProduct.entity.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Flux<Product> getAll();
    Mono<Product> createProduct(Product new_product);
}
