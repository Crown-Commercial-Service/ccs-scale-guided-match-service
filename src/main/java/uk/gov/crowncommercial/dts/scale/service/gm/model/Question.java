package uk.gov.crowncommercial.dts.scale.service.gm.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

/**
 *
 */
@JsonDeserialize(builder = Question._Builder.class)
@Value
@Builder(builderClassName = "_Builder")
public class Question {

  String id;
  String text;
  String hint;
  QuestionType type;

  @JsonPOJOBuilder(withPrefix = "")
  public static class _Builder {
    // Enhanced by Lombok
  }


}
