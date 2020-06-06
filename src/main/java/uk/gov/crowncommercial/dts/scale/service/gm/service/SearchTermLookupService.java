package uk.gov.crowncommercial.dts.scale.service.gm.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.gm.model.GetJourneySummaryResponse;
import uk.gov.crowncommercial.dts.scale.service.gm.model.entity.SearchDomain;
import uk.gov.crowncommercial.dts.scale.service.gm.repository.SearchDomainRepo;

/**
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SearchTermLookupService {

  private final SearchDomainRepo searchDomainRepo;

  public GetJourneySummaryResponse getJourneySummary(final Integer lookupEntryId) {

    SearchDomain searchDomain = searchDomainRepo.findById(lookupEntryId)
        .orElseThrow(() -> new RuntimeException("TODO: 404 No search domain record found"));

    log.debug("Found SearchDomain record: {}", searchDomain.toString());

    return new GetJourneySummaryResponse(searchDomain.getJourney().getId().toString(),
        searchDomain.getModifierJourneyName(), searchDomain.getModifierJourneyDescription(),
        "TODO: Unknown");
  }

}
