package uk.gov.crowncommercial.dts.scale.service.gm.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

/**
 *
 */
@JsonDeserialize(builder = JourneySelectionParameters.JourneySelectionParametersBuilder.class)
@Value
@Builder
public class JourneySelectionParameters {

  String searchTerm;
  String modifier;

  @JsonPOJOBuilder(withPrefix = "")
  public static class JourneySelectionParametersBuilder {
    // Enhanced by Lombok
  }


}
