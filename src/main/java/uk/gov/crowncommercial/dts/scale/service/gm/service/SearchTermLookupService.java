package uk.gov.crowncommercial.dts.scale.service.gm.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.gm.model.GetJourneySummaryResponse;
import uk.gov.crowncommercial.dts.scale.service.gm.repository.SearchDomainRepo;
import uk.gov.crowncommercial.dts.scale.service.gm.temp.DataLoader;

/**
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SearchTermLookupService {

  private final SearchDomainRepo searchDomainRepo;

  private final DataLoader dataLoader;

  public GetJourneySummaryResponse getJourneySummary(final String lookupEntryId) {

    searchDomainRepo.findAll().stream().forEach(sd -> log.info(sd.toString()));

    return dataLoader.convertJsonToObject("get-journey-summary/" + lookupEntryId + ".json",
        GetJourneySummaryResponse.class);
  }

}
