package org.websitescrapers.nordstrom.config;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebdriverConfig {

  public static ChromeDriver getDriver() {
    final ChromeDriver driver = new ChromeDriver(getDriverOptions());
    driver.manage().window().maximize();
    return driver;
  }

  private static ChromeOptions getDriverOptions() {
    final ChromeOptions options = new ChromeOptions();
    options.setHeadless(true);
    options.addArguments("--disable-bundled-ppapi-flash"); // disable flash
    return options;
  }
}
