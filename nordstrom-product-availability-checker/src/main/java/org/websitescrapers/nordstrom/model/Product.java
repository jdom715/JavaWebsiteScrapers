package org.websitescrapers.nordstrom.model;

import java.util.Map;

public class Product {
  private String description;
  private String size;
  private String color;
  private String url;
  private Map<String, String> requesters;

  Product(
      final String description,
      final String size,
      final String color,
      final String url,
      final Map<String, String> requesters) {
    this.description = description;
    this.size = size;
    this.color = color;
    this.url = url;
    this.requesters = requesters;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Map<String, String> getRequesters() {
    return requesters;
  }

  public void setRequesters(Map<String, String> requesters) {
    this.requesters = requesters;
  }
}
