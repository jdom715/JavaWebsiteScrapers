package org.websitescrapers.nordstrom.model;

import java.util.Map;

public class ProductBuilder {

  private String description;
  private String size;
  private String color;
  private String url;
  private Map<String, String> requesters;

  public ProductBuilder description(String description) {
    this.description = description;
    return this;
  }

  public ProductBuilder size(String size) {
    this.size = size;
    return this;
  }

  public ProductBuilder color(String color) {
    this.color = color;
    return this;
  }

  public ProductBuilder url(String url) {
    this.url = url;
    return this;
  }

  public ProductBuilder requesters(Map<String, String> requesters) {
    this.requesters = requesters;
    return this;
  }

  public Product build() {
    return new Product(description, size, color, url, requesters);
  }
}
