package org.websitescrapers.nordstrom.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.Map;
import org.websitescrapers.nordstrom.exception.BadStyleException;
import org.websitescrapers.nordstrom.model.Product;
import org.websitescrapers.nordstrom.model.ProductBuilder;
import org.websitescrapers.nordstrom.model.StyleType;

@DynamoDBTable(tableName = "product-requests")
public class ProductRequest {
  @DynamoDBHashKey(attributeName = "product-url")
  private String productUrl;

  /** In the format "size-color" */
  @DynamoDBRangeKey(attributeName = "product-style")
  private String productStyle;

  @DynamoDBAttribute(attributeName = "product-description")
  private String productDescription;

  @DynamoDBAttribute(attributeName = "requesters")
  private Map<String, String> requesters;

  public ProductRequest() {}

  public String getProductUrl() {
    return productUrl;
  }

  public void setProductUrl(String productUrl) {
    this.productUrl = productUrl;
  }

  public String getProductStyle() {
    return productStyle;
  }

  public void setProductStyle(String productStyle) {
    this.productStyle = productStyle;
  }

  public Map<String, String> getRequesters() {
    return requesters;
  }

  public void setRequesters(Map<String, String> requesters) {
    this.requesters = requesters;
  }

  public String getProductDescription() {
    return productDescription;
  }

  public void setProductDescription(String productDescription) {
    this.productDescription = productDescription;
  }

  public Product toProduct() throws BadStyleException {
    final String[] styleTypes = this.productStyle.split("-");
    if (styleTypes.length != StyleType.values().length) {
      throw new BadStyleException(
          String.format("The style type range key was bad: %s.", styleTypes.length));
    }

    final String size = styleTypes[0];
    final String color = styleTypes[1];
    return new ProductBuilder()
        .color(color)
        .description(this.productDescription)
        .requesters(this.requesters)
        .size(size)
        .url(this.productUrl)
        .build();
  }
}
