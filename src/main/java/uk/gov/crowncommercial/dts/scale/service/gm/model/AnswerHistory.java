package uk.gov.crowncommercial.dts.scale.service.gm.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

/**
 *
 */
@JsonDeserialize(builder = AnswerHistory._Builder.class)
@Value
@Builder(builderClassName = "_Builder")
public class AnswerHistory {

  String answer;
  String answerText;

  @JsonPOJOBuilder(withPrefix = "")
  public static class _Builder {
    // Enhanced by Lombok
  }

}
