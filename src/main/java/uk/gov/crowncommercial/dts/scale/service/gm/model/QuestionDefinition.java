package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.Set;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

/**
 *
 */
@JsonDeserialize(builder = QuestionDefinition._Builder.class)
@Value
@Builder(builderClassName = "_Builder")
public class QuestionDefinition {

  Question question;
  String pattern;
  String prePopulatedAnswerId;
  String prePopulatedAnswerSource;
  Set<AnswerDefinition> definedAnswers;

  @JsonPOJOBuilder(withPrefix = "")
  public static class _Builder {
    // Enhanced by Lombok
  }

}
