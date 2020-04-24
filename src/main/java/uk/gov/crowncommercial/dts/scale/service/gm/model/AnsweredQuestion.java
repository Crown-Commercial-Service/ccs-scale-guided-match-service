package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.Set;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

/**
 *
 */
@JsonDeserialize(builder = AnsweredQuestion.AnsweredQuestionBuilder.class)
@Value
@Builder
public class AnsweredQuestion {

  String id;
  Set<String> answers;

  @JsonPOJOBuilder(withPrefix = "")
  public static class AnsweredQuestionBuilder {
    // Enhanced by Lombok
  }


}
