package uk.gov.crowncommercial.dts.scale.service.gm.controller.model;

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
