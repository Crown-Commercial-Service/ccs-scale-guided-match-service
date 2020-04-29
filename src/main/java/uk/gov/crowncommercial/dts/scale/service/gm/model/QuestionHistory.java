package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.Set;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

/**
 *
 */
@JsonDeserialize(builder = QuestionHistory._Builder.class)
@Value
@Builder(builderClassName = "_Builder")
public class QuestionHistory {

  Question question;
  Set<AnswerHistory> answers;
  String variableName;

  @JsonPOJOBuilder(withPrefix = "")
  public static class _Builder {
    // Enhanced by Lombok
  }

}
