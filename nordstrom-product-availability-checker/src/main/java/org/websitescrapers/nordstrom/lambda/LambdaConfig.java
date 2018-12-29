package org.websitescrapers.nordstrom.lambda;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.websitescrapers.nordstrom.repo.ProductRequestsRepo;
import org.websitescrapers.nordstrom.scraper.ProductAvailabilityChecker;
import org.websitescrapers.nordstrom.util.EmailClient;

@Configuration
@Import({EmailClient.class, ProductAvailabilityChecker.class, ProductRequestsRepo.class})
public class LambdaConfig {
}
