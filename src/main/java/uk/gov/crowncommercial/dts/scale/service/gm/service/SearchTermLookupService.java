package uk.gov.crowncommercial.dts.scale.service.gm.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import uk.gov.crowncommercial.dts.scale.service.gm.controller.model.GetJourneySummaryResponse;
import uk.gov.crowncommercial.dts.scale.service.gm.temp.DataLoader;

/**
 *
 */
@Service
@RequiredArgsConstructor
public class SearchTermLookupService {

  private final DataLoader dataLoader;

  public GetJourneySummaryResponse getJourneySummary(final String lookupEntryId) {

    return dataLoader.convertJsonToObject("get-journey-summary/" + lookupEntryId + ".json",
        GetJourneySummaryResponse.class);
  }

}
