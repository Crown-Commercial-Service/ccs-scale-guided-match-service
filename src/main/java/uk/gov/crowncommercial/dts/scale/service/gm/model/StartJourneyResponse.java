package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.Set;
import lombok.Value;

/**
 *
 */
@Value
public class StartJourneyResponse {

  String journeyInstanceId;
  Set<Question> questions;

}
