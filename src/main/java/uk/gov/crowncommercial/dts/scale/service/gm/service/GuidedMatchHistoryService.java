package uk.gov.crowncommercial.dts.scale.service.gm.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import uk.gov.crowncommercial.dts.scale.service.gm.model.GetJourneyHistoryResponse;
import uk.gov.crowncommercial.dts.scale.service.gm.temp.DataLoader;

/**
 *
 */
@Service
@RequiredArgsConstructor
public class GuidedMatchHistoryService {

  private final DataLoader dataLoader;

  public GetJourneyHistoryResponse getJourneyHistory(final String journeyInstanceId) {

    return dataLoader.convertJsonToObject("get-journey-history/" + journeyInstanceId + ".json",
        GetJourneyHistoryResponse.class);
  }

}
