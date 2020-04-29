package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

/**
 *
 */
@JsonDeserialize(builder = GetJourneyQuestionOutcomeResponse._Builder.class)
@Value
@Builder(builderClassName = "_Builder")
public class GetJourneyQuestionOutcomeResponse {

  Outcome outcome;
  List<QuestionHistory> questionHistory;

  @JsonPOJOBuilder(withPrefix = "")
  public static class _Builder {
    // Enhanced by Lombok
  }

}
