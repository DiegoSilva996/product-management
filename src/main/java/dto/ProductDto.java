package dto;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

public class ProductDto {
    
    private String id;
    private String clientId;
    private Date creationDate;
    private String transactionDate; 
    private int maximumTransactionLimit;
    private int numberOfFreeTransactions;
    private Double maintenanceCommission;
    private Double amount;
    private String productType;
    private String status;
    private List<String> owners;
    private List<String> authorizedSigner;
}
