package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.List;
import lombok.Value;

/**
 *
 */
@Value
public class GetJourneyQuestionOutcomeResponse {

  Outcome outcome;
  List<QuestionHistory> journeyHistory;

}
