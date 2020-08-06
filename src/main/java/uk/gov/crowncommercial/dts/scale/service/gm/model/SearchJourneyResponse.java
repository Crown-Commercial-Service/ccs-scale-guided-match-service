package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.UUID;
import lombok.Data;

/**
 *
 */
@Data
public class SearchJourneyResponse {

  UUID journeyId;
  String modifier;
  String selectionText;
  String selectionDescription;

  // for testing
  String searchTerm;

}
