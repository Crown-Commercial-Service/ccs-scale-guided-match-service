package uk.gov.crowncommercial.dts.scale.service.gm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import lombok.Value;

/**
 *
 */
@ConfigurationProperties("external.decision-tree-service")
@ConstructorBinding
@Value
public class DecisionTreeServiceConfig {

  @Value
  public static class UriTemplates {
    private final String getJourney;
    private final String getJourneyQuestionOutcome;
    private final String getJourneyQuestion;
  }

  private final String url;
  private final UriTemplates uriTemplates;

}
