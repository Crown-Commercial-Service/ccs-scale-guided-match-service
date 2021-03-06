package uk.gov.crowncommercial.dts.scale.service.gm.model.external.dt;

import lombok.Value;

/**
 * Journey with embedded first question
 */
@Value
public class DTJourney {

  String uuid;
  String name;
  DTQuestionDefinition firstQuestion;

}
