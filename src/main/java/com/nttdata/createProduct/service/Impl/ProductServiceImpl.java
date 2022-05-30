package com.nttdata.createProduct.service.Impl;



import com.nttdata.createProduct.entity.Product;
import com.nttdata.createProduct.repository.ClientRepository;
import com.nttdata.createProduct.repository.ProductRepository;
import com.nttdata.createProduct.service.ProductService;

import ch.qos.logback.classic.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class ProductServiceImpl implements ProductService{
    
	@Autowired
    private ProductRepository productRepository;
	@Autowired
    private ClientRepository clientRepository;
	
	private Logger log= (Logger) LoggerFactory.getLogger(ProductServiceImpl.class);
	
    @Override
    public Flux<Product> getAll() {
      return productRepository.findAll();
    }

    @Override
    public Mono<Product> createProduct(Product product){

        log.info("entrando a método createProduct");
        Map<String, Object> salida = new HashMap<>();      
        HashMap<String, Object> data_client = validateCustomer(product.getClientId());  
        String message = (data_client.get("message")).toString();

        if(message == "Id de cliente no encontrado"){
            log.info("id incorrecto");
            salida.put("message", "Id de cliente no encontrado");  
        }else{
            
            String ClientType= (data_client.get("ClientType")).toString();         
            String IdClient= (data_client.get("IdClient")).toString();      
            
            int cant_cuenta_ahorro= (int) data_client.get("cant_cuenta_ahorro");
            int cant_cuenta_corriente= (int) data_client.get("cant_cuenta_corriente");
            //int cant_cuenta_plazo_fijo=(int) data_client.get("cant_cuenta_plazo_fijo");

            log.info("entro al else");
            String productType = product.getProductType();
            log.info(productType);

            //Asignar fecha de creación
            java.util.Date date = new java.util.Date();
            product.setCreationDate(date);
            //Asignar status
            product.setStatus("ACTIVE");
            //Las cuentas bancarias tienen un monto mínimo de apertura que puede ser cero (0).
            if(productType.equals("BUSINESS_CREDIT")==false && productType.equals("PERSONAL_CREDIT")==false && productType.equals("CREDIT_CARD")==false){
                if(product.getAmount().isNaN()){
                	product.setAmount(0.00);
                }
            }

            //Productos del tipo cuenta
            if(productType.equals("CURRENT_ACCOUNT" )){
                log.info("1");
                HashMap<String, Object> create_product_a = createCurrentAccount(  product, cant_cuenta_corriente, ClientType );
                salida.put("ouput", create_product_a);
            }else if(productType.equals("SAVING_ACCOUNT")){
                log.info("2");
                HashMap<String, Object> create_product_b = createSavingAccount(  product,  cant_cuenta_ahorro, ClientType);
                salida.put("ouput", create_product_b);
            }else if(productType.equals("SAVING_ACCOUNT_VIP")){
                log.info("2");
                HashMap<String, Object> create_product_b = createSavingAccountVIP(  product,   ClientType);
                salida.put("ouput", create_product_b);
            }else if(productType.equals("SAVING_ACCOUNT_VIP")){
                log.info("2");
                HashMap<String, Object> create_product_b = createCurrentAccountPYME(  product,   ClientType);
                salida.put("ouput", create_product_b);
            }else if(productType.equals("FIXED_TERM_ACCOUNT")){
                log.info("3");
                HashMap<String, Object> create_product_c = createFixedTermAccount(  product, ClientType );
                salida.put("ouput", create_product_c);
            }
            //Productos del tipo crédito
            else if(productType.equals("BUSINESS_CREDIT") || productType.equals("PERSONAL_CREDIT")){
                log.info("4");
                HashMap<String, Object> create_product_d = createCredit(  product, ClientType, IdClient );
                salida.put("ouput", create_product_d);
            }else if(productType.equals("CREDIT_CARD")){
                log.info("5");
                HashMap<String, Object> create_product_e = createCreditCard(  product, ClientType, IdClient );
                salida.put("ouput", create_product_e);
            }

        } 
        
        return productRepository.save(product);
    }
    
    public Mono<Product> getProductData(String id) {
		return productRepository.findById(id);
	}
    
	public Mono<Product> updateProduct(Product product,String id){
		
	
		return productRepository.findById(id)
				.map(p->{
					p.setAmount(product.getAmount());
					p.setAuthorizedSigner(product.getAuthorizedSigner());
					p.setClientId(product.getClientId());
					p.setCreationDate(product.getCreationDate());
					p.setId(product.getId());
					p.setMaintenanceCommission(product.getMaintenanceCommission());
					p.setMaximumTransactionLimit(product.getMaximumTransactionLimit());
					p.setNumberOfFreeTransactions(product.getNumberOfFreeTransactions());
					p.setOwners(product.getOwners());
					p.setProductType(product.getProductType());
					p.setStatus("ACTIVE");
					p.setTransactionDate(product.getTransactionDate());
					return p;
				});
	}
	
	public Mono<Void> deleteProduct(String id){
		return productRepository.deleteById(id);
	}
	
	public Mono<Product> setInactiveProduct(String id){
		return productRepository.findById(id)
				.doOnNext(e->e.setStatus("INACTIVE"))
				.flatMap(productRepository::save);
	}
	


    //Clase interna para validar cliente y cuentas
    public HashMap<String, Object> validateCustomer(String id) {        
        HashMap<String, Object> map = new HashMap<>();
        
        //Optional<Client> client_doc = clientRepo.findById(id);
        
        int Q_1 =  productRepository.findByProductTypeAndClientId("SAVING_ACCOUNT",id).collectList().block().size();
        int Q_2 =  productRepository.findByProductTypeAndClientId("CURRENT_ACCOUNT",id).collectList().block().size();
        int Q_3 =  productRepository.findByProductTypeAndClientId("FIXED_TERM_ACCOUNT",id).collectList().block().size();;
        
        //Armar hashmap - probar clientType
        map.put("message", "Id de cliente encontrado");
        map.put("IdClient", id);
        map.put("ClientType", clientRepository.findById(id).doOnNext(e->e.getClientType()));
        map.put("cant_cuenta_ahorro", Q_1);
        map.put("cant_cuenta_corriente", Q_2);
        map.put("cant_cuenta_plazo_fijo", Q_3);
        
        /*if (client_doc.isPresent()) {

            Customer current_cli = Customer.class.cast(client_doc.get());
            
            //Existe, obtener cantidad de cuentas
 
        }else{
            map.put("message", "Id de cliente no encontrado");
        }*/
        return map;
    }

    
    //Clase interna para crear cuenta del tipo cuenta de ahorro
    public HashMap<String, Object> createSavingAccount (@RequestBody Product new_product, int cant_cuentas, String ClientType  ){
        HashMap<String, Object> map = new HashMap<>();
        try{
            if(ClientType.equals("BUSINESS")){
                map.put("mensaje", "Cuenta de ahorro no habilitada para empresas.");
            }
            else if(ClientType.equals("PERSON") && cant_cuentas == 0){
                new_product.setMaintenanceCommission(0.0);                 
                productRepository.save(new_product);
                map.put("account", new_product);
            }else{
                map.put("mensaje", "El cliente ya tiene una cuenta de ahorro");
            }

        }catch(Exception e) {
            e.printStackTrace();
            map.put("mensaje", "error");
        }                    
        return map;
    }
    
    
    
    //Clase interna para crear cuenta del tipo cuenta de ahorro VIP
    public HashMap<String, Object> createSavingAccountVIP (@RequestBody Product new_product, String ClientType  ){
        HashMap<String, Object> map = new HashMap<>();
        try{
            //Validar si tiene tarjeta de credito 
            if(ClientType.equals("BUSINESS")){
                map.put("mensaje", "Cuenta de ahorro VIP no habilitada para empresas.");
            }else if(ClientType.equals("PERSON")){
                if(productRepository.findByProductTypeAndClientId("CREDIT_CARD",new_product.getClientId()).collectList().block().size() == 0){
                    map.put("mensaje", "El cliente no tiene tarjeta de crédito");
                }
                else{
                    new_product.setMaintenanceCommission(0.0);                 
                    productRepository.save(new_product);
                    map.put("account", new_product);
                }
            }

        }catch(Exception e) {
            e.printStackTrace();
            map.put("mensaje", "error");
        }                    
        return map;
    }
    
    //Clase interna para crear cuenta del tipo cuenta de ahorro VIP
    public HashMap<String, Object> createCurrentAccountPYME (@RequestBody Product new_product, String ClientType  ){
        HashMap<String, Object> map = new HashMap<>();
        try{
            //Validar si tiene tarjeta de credito
          
        	if(ClientType.equals("PERSON")){
                map.put("mensaje", "Cuenta corriente PYME no habilitada para personas.");
            }else if(ClientType.equals("BUSINESS")){
                if(productRepository.findByProductTypeAndClientId("CREDIT_CARD",new_product.getClientId()).collectList().block().size() == 0){
                    map.put("mensaje", "El cliente no tiene tarjeta de crédito");
                }
                else{
                    new_product.setMaintenanceCommission(0.0);                 
                    productRepository.save(new_product);
                    map.put("account", new_product);
                }
            }

        }catch(Exception e) {
            e.printStackTrace();
            map.put("mensaje", "error");
        }                    
        return map;
    }
    
    //Clase interna para crear cuenta del tipo cuenta corriente
    public HashMap<String, Object> createCurrentAccount(@RequestBody Product new_product,  int cant_cuentas, String ClientType  ){
        HashMap<String, Object> map = new HashMap<>();
        try{
            if(ClientType.equals("BUSINESS")){
                map.put("mensaje", "Cuenta corriente no habilitada para empresas.");
            }
            else if(ClientType.equals("PERSON") && cant_cuentas == 0){
                new_product.setMaximumTransactionLimit(0); 
                productRepository.save(new_product);
                map.put("account", productRepository.save(new_product));
            }else{
                map.put("mensaje", "El cliente ya tiene una cuenta corriente.");
            }
        }catch(Exception e) {
            e.printStackTrace();
            map.put("mensaje", "error");
        }             
        return map;
    }  
    //Clase interna para crear cuenta del tipo cuenta plazo fijo
    public HashMap<String, Object> createFixedTermAccount(@RequestBody Product new_product, String ClientType ){
        HashMap<String, Object> map = new HashMap<>();
        try{
            if(ClientType.equals("BUSINESS")){
                map.put("mensaje", "Cuenta de plazo fijo no habilitada para empresas.");
            }else{
                new_product.setMaintenanceCommission(0.0); 
                new_product.setMaximumTransactionLimit(0);  
                productRepository.save(new_product);
                map.put("account", new_product);
            }
            
        }catch(Exception e) {
            e.printStackTrace();
            map.put("mensaje", "error");
        }                    
        return map;
    }
    
    
    //Clase interna para crear creditos
    public HashMap<String, Object> createCredit(@RequestBody Product new_product, String ClientType, String IdClient ){
        HashMap<String, Object> map = new HashMap<>();
        try{
        
        	if(ClientType.equals("BUSINESS") && productRepository.findByProductTypeAndClientId("BUSINESS_CREDIT",IdClient).collectList().block().size() == 0){
               new_product.setProductType("BUSINESS_CREDIT");
               productRepository.save(new_product);
               map.put("account", new_product);
            }
            else if(ClientType.equals("PERSON")&& productRepository.findByProductTypeAndClientId("PERSONAL_CREDIT",IdClient).collectList().block().size() == 0){
               new_product.setProductType("PERSONAL_CREDIT");
               productRepository.save(new_product);
               map.put("account", new_product);
            }else{
                map.put("mensaje", "El cliente ya tiene un producto de crédito habilitado."); 
            }            
            
            
        }catch(Exception e) {
            e.printStackTrace();
            map.put("mensaje", "error");
        }                    
        return map;
    }
    

    
    //Clase interna para crear una tarjeta de crédito
    public HashMap<String, Object> createCreditCard(@RequestBody Product new_product, String ClientType, String IdClient ){
        HashMap<String, Object> map = new HashMap<>();
        try{

            if(productRepository.findByProductTypeAndClientId("CREDIT_CARD",IdClient).collectList().block().size() == 0){
               new_product.setProductType("BUSINESS_CREDIT");
               productRepository.save(new_product);
               map.put("account", new_product);
            }else{
                map.put("mensaje", "El cliente ya tiene una tarjeta de crédito habilitada."); 
            }                 
            
        }catch(Exception e) {
            e.printStackTrace();
            map.put("mensaje", "error");
        }                    
        return map;
    } 
    
}
