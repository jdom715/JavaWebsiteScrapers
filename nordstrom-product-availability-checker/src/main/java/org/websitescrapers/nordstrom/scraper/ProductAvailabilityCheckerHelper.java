package org.websitescrapers.nordstrom.scraper;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.websitescrapers.nordstrom.model.Product;
import org.websitescrapers.nordstrom.model.StyleType;

class ProductAvailabilityCheckerHelper {
  private static Logger logger = LogManager.getLogger();

  private ProductAvailabilityCheckerHelper() {}

  static boolean productIsAvailable(final RemoteWebDriver driver, Product productWanted) {
    return styleIsAvailable(driver, StyleType.SIZE, productWanted.getSize())
        && styleIsAvailable(driver, StyleType.COLOR, productWanted.getColor());
  }

  private static String getDescription(final RemoteWebDriver driver) {
    final String css_selector = "h1[itemprop='name']";
    final WebElement product_description_header = driver.findElementByCssSelector(css_selector);
    return product_description_header.getText();
  }

  private static boolean styleIsAvailable(
      final RemoteWebDriver driver, final StyleType styleType, final String styleValueWanted) {
    final String cssSelector =
        String.format("div[data-element='%s-sku-filter-dropdown']", styleType.name().toLowerCase());
    final WebElement dropdownList = driver.findElementByCssSelector(cssSelector);
    click(driver, dropdownList);

    final List<WebElement> styleList =
        driver.findElementsByXPath("//div[starts-with(@class, 'SkuFilterListItemBody')]");
    logger.info("Found {} styles", styleList.size());
    for (final WebElement style : styleList) {
      if (styleValueIsAvailableFromList(style, styleValueWanted)) {
        logger.info("Found wanted style value {}; clicking.", styleValueWanted);
        click(driver, style);
        return true;
      }
    }
    return false;
  }

  private static boolean styleValueIsAvailableFromList(
      final WebElement style, final String styleValueWanted) {
    final String styleValue = style.getText().toLowerCase();
    logger.info("On style value {}", styleValue);
    return styleValue.contains(styleValueWanted) && !styleValue.contains("not available");
  }

  private static void click(final RemoteWebDriver driver, final WebElement ele) {
    driver.executeScript("arguments[0].click();", ele);
  }
}
