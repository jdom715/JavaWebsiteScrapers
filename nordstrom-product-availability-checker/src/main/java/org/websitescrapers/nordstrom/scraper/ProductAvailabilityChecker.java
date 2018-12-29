package org.websitescrapers.nordstrom.scraper;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import javax.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Controller;
import org.websitescrapers.nordstrom.config.WebdriverConfig;
import org.websitescrapers.nordstrom.dao.ProductRequest;
import org.websitescrapers.nordstrom.exception.BadStyleException;
import org.websitescrapers.nordstrom.model.Product;
import org.websitescrapers.nordstrom.repo.ProductRequestsRepo;
import org.websitescrapers.nordstrom.util.EmailClient;

@Controller
public class ProductAvailabilityChecker {
  private static final double NANOSECONDS_TO_SECONDS = .000000001;
  private static final Logger logger = LogManager.getLogger();
  private final RemoteWebDriver driver;
  private final EmailClient emailClient;
  private final ProductRequestsRepo productRequestsRepo;

  public ProductAvailabilityChecker(
      final EmailClient emailClient,
      final ProductRequestsRepo productRequestsRepo) {
    this.driver = WebdriverConfig.getDriver();
    this.emailClient = emailClient;
    this.productRequestsRepo = productRequestsRepo;
  }

  public void checkAllProductAvailability() {
    final long start = System.nanoTime();
    try {
      final PaginatedScanList<ProductRequest> productRequests = productRequestsRepo.getAll();
      for (ProductRequest productRequest : productRequests) {
        checkProductAvailability(productRequest);
      }
    } catch (final Exception e) {
      handleException(e);
    } finally {
      logAndCleanup(start);
    }
  }

  private void checkProductAvailability(final ProductRequest productRequest)
      throws BadStyleException, MessagingException {
    driver.get(productRequest.getProductUrl());

    final Product productWanted = productRequest.toProduct();
    if (ProductAvailabilityCheckerHelper.productIsAvailable(driver, productWanted)) {
      logger.info("Style {} is available for product {}. Sending availability email", productRequest.getProductStyle(), productRequest.getProductDescription());
      emailClient.sendProductAvailableEmail(productWanted);
    } else {
      logger.info("Style {} is not available for product {}.", productRequest.getProductStyle(), productRequest.getProductDescription());
    }
  }

  private void handleException(final Exception e) {
    e.printStackTrace();
    try {
      emailClient.sendErrorEmail(e);
    } catch (MessagingException e1) {
      e1.printStackTrace();
    }
  }

  private void logAndCleanup(final long start) {
    final long end = System.nanoTime();
    logger.info(
        "Nordstrom product availability checker took {} seconds.",
        (end - start) * NANOSECONDS_TO_SECONDS);
    driver.close();
    driver.quit();
  }
}
