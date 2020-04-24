package uk.gov.crowncommercial.dts.scale.service.gm.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

/**
 *
 */
@JsonDeserialize(builder = SearchResults.SearchResultsBuilder.class)
@Value
@Builder
public class SearchResults {
  String term;
  String result;

  @JsonPOJOBuilder(withPrefix = "")
  public static class SearchResultsBuilder {
    // Enhanced by Lombok
  }

}
