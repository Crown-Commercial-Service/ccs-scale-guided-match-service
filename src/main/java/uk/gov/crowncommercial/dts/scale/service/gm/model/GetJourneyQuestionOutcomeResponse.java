package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.Set;
import lombok.Value;

/**
 *
 */
@Value
public class GetJourneyQuestionOutcomeResponse {

  Outcome outcome;
  Set<QuestionHistory> journeyHistory;

}
