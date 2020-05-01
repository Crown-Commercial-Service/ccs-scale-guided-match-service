package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.Set;
import lombok.Value;

/**
 *
 */
@Value
public class GetJourneyHistoryResponse {

  String searchTerm;
  String userId;
  Outcome outcome;
  Set<QuestionHistory> journeyHistory;

}
