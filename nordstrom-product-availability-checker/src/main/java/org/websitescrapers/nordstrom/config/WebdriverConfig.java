package org.websitescrapers.nordstrom.config;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebdriverConfig {

  @Bean
  public static FirefoxDriver getFirefoxDriver() {
    final FirefoxDriver driver = new FirefoxDriver(getFirefoxOptions());
    driver.manage().window().maximize();
    return driver;
  }

  private static FirefoxOptions getFirefoxOptions() {
    final FirefoxOptions firefoxOptions = new FirefoxOptions();
    firefoxOptions.addArguments("-headless");
    firefoxOptions.setProfile(getFirefoxProfile());
    return firefoxOptions;
  }

  private static FirefoxProfile getFirefoxProfile() {
    final FirefoxProfile profile = new FirefoxProfile();
    profile.setPreference("permissions.default.image", 2);
    profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so", "false");
    return profile;
  }
}
