package uk.gov.crowncommercial.dts.scale.service.gm.model;

import lombok.Value;

/**
 *
 */
@Value
public class StartJourneyResponse {

  String journeyInstanceId;
  QuestionDefinitionList questions;

}
