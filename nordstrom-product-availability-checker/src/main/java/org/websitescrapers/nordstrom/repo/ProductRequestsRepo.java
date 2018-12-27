package org.websitescrapers.nordstrom.repo;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import org.springframework.stereotype.Repository;
import org.websitescrapers.nordstrom.dao.ProductRequest;

@Repository
public class ProductRequestsRepo {
  private final DynamoDBMapper mapper;

  public ProductRequestsRepo() {
    final AmazonDynamoDB ddb =
        AmazonDynamoDBClientBuilder.standard()
            .withCredentials(new EnvironmentVariableCredentialsProvider())
            .withRegion(Regions.US_WEST_2)
            .build();
    mapper = new DynamoDBMapper(ddb);
  }

  public PaginatedScanList<ProductRequest> getAll() throws SdkClientException {
    return mapper.scan(ProductRequest.class, new DynamoDBScanExpression());
  }
}
