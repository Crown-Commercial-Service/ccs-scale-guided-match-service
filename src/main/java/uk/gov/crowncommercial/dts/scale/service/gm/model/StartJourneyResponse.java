package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.Set;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

/**
 *
 */
@JsonDeserialize(builder = StartJourneyResponse._Builder.class)
@Value
@Builder(builderClassName = "_Builder")
public class StartJourneyResponse {

  String journeyInstanceId;
  Set<QuestionDefinition> questions;

  @JsonPOJOBuilder(withPrefix = "")
  public static class _Builder {
    // Enhanced by Lombok
  }

}
