package uk.gov.crowncommercial.dts.scale.service.gm.model;

import lombok.Value;

/**
 *
 */
@Value
public class GetJourneySummaryResponse {

  String journeyId;
  String modifier;
  String selectionText;
  String selectionDescription;

}
