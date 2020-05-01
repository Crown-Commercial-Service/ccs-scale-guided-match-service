package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.time.Instant;
import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import lombok.Data;

/**
 * Varying-type outcome (agreement, question, support)
 */
@Data
public class Outcome {

  OutcomeType outcomeType;
  Instant timestamp;

  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.EXTERNAL_PROPERTY,
      property = "outcomeType")
  @JsonSubTypes({@JsonSubTypes.Type(value = QuestionDefinitionList.class, name = "question"),
      @JsonSubTypes.Type(value = AgreementList.class, name = "agreement")})
  @Nullable
  OutcomeData data;

}
