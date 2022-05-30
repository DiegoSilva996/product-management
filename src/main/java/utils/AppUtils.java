package utils;
import org.springframework.beans.BeanUtils;

import com.nttdata.createProduct.entity.Customer;
import com.nttdata.createProduct.entity.Product;

import dto.ClientDto;
import dto.ProductDto;



public class AppUtils {


	

		public static ProductDto productEntitytoDto(Product product) {
			ProductDto productDto=new ProductDto();
			BeanUtils.copyProperties(product, productDto);
			return productDto;
		}
		
		public static Product DtoToproductEntity(ProductDto productDto) {
			Product product=new Product();
			BeanUtils.copyProperties(productDto, product);
			return product;
		}
		
		public static ClientDto clientEntitytoDto(Customer client) {
			ClientDto clientDto=new ClientDto();
			BeanUtils.copyProperties(client, clientDto);
			return clientDto;
		}
		
		public static Customer DtoToclientEntity(ClientDto clientdto) {
			Customer client=new Customer();
			BeanUtils.copyProperties(clientdto, client);
			return client;
		}
		
		
	
}
