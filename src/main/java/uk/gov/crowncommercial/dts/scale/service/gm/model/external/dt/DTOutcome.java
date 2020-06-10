package uk.gov.crowncommercial.dts.scale.service.gm.model.external.dt;

import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import lombok.Value;
import uk.gov.crowncommercial.dts.scale.service.gm.model.AgreementList;
import uk.gov.crowncommercial.dts.scale.service.gm.model.OutcomeData;
import uk.gov.crowncommercial.dts.scale.service.gm.model.OutcomeType;

/**
 * Varying-type outcome (question, agreement, support)
 */
@Value
public class DTOutcome {

  OutcomeType outcomeType;

  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.EXTERNAL_PROPERTY,
      property = "outcomeType")
  @JsonSubTypes({@JsonSubTypes.Type(value = DTQuestionDefinitionList.class, name = "question"),
      @JsonSubTypes.Type(value = AgreementList.class, name = "agreement")})
  @Nullable
  OutcomeData data;

}
