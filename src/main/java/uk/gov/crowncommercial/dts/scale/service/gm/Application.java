package uk.gov.crowncommercial.dts.scale.service.gm;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * SpringBoot application entry point.
 *
 */
@SpringBootApplication
public class Application {

  public static void main(final String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public Clock utcClock() {
    return Clock.systemUTC();
  }
}
