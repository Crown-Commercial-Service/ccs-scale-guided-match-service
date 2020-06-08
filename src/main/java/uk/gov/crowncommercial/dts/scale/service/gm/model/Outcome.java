package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.time.Instant;
import lombok.Builder;
import lombok.Value;

/**
 * Varying-type outcome (agreement, question, support)
 */
@Value
@Builder
public class Outcome {

  OutcomeType outcomeType;
  Instant timestamp;
  OutcomeData data;

}
