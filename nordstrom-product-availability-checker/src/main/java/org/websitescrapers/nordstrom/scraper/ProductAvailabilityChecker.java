package org.websitescrapers.nordstrom.scraper;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import javax.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.stereotype.Controller;
import org.websitescrapers.nordstrom.config.WebdriverConfig;
import org.websitescrapers.nordstrom.dao.ProductRequest;
import org.websitescrapers.nordstrom.exception.BadStyleException;
import org.websitescrapers.nordstrom.model.Product;
import org.websitescrapers.nordstrom.repo.ProductRequestsRepo;
import org.websitescrapers.nordstrom.util.EmailClient;

@Controller
public class ProductAvailabilityChecker implements RequestHandler<String, String> {
  private static final double NANOSECONDS_TO_SECONDS = .000000001;
  private static final Logger logger = LogManager.getLogger();
  private final FirefoxDriver driver;
  private final EmailClient emailClient;
  private final ProductRequestsRepo productRequestsRepo;

  public ProductAvailabilityChecker(
      final EmailClient emailClient,
      final ProductRequestsRepo productRequestsRepo) {
    this.driver = WebdriverConfig.getFirefoxDriver();
    this.emailClient = emailClient;
    this.productRequestsRepo = productRequestsRepo;
  }

  @Override
  public String handleRequest(final String request, final Context context) {
    final long start = System.nanoTime();
    try {
      checkProductAvailability();
      return "";
    } catch (final Exception e) {
      e.printStackTrace();
      try {
        emailClient.sendErrorEmail(e);
      } catch (MessagingException e1) {
        e1.printStackTrace();
      }
      return "";
    } finally {
      final long end = System.nanoTime();
      logger.info(
          "Nordstrom product availability checker took {} seconds.",
          (end - start) * NANOSECONDS_TO_SECONDS);
      driver.quit();
    }
  }

  private void checkProductAvailability() throws BadStyleException, MessagingException {
    final PaginatedScanList<ProductRequest> productRequests = productRequestsRepo.getAll();
    for (ProductRequest productRequest : productRequests) {
      driver.get(productRequest.getProductUrl());

      final Product productWanted = productRequest.toProduct();

      if (ProductAvailabilityCheckerHelper.productIsAvailable(driver, productWanted)) {
        emailClient.sendProductAvailableEmail(productWanted);
      } else {
        logger.info("Style {} is not available for product {}.", productRequest.getProductStyle(), productRequest.getProductDescription());
      }
    }
  }
}
