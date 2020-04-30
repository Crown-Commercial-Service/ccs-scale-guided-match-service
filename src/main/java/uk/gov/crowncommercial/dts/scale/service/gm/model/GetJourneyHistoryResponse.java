package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.Set;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

/**
 *
 */
@JsonDeserialize(builder = GetJourneyHistoryResponse._Builder.class)
@Value
@Builder(builderClassName = "_Builder")
public class GetJourneyHistoryResponse {

  String searchTerm;
  String userId;
  Outcome outcome;
  Set<QuestionHistory> journeyHistory;

  @JsonPOJOBuilder(withPrefix = "")
  public static class _Builder {
    // Enhanced by Lombok
  }


}
