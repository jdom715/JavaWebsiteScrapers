package org.websitescrapers.nordstrom;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.websitescrapers.nordstrom.scraper.ProductAvailabilityChecker;

@ComponentScan
public class App {
  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(App.class);
    context.getBean(ProductAvailabilityChecker.class).checkAllProductAvailability();
  }
}
