package uk.gov.crowncommercial.dts.scale.service.gm.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.gm.model.GetJourneySummaryResponse;
import uk.gov.crowncommercial.dts.scale.service.gm.model.SearchJourneyResponse;
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

  public GetJourneySummaryResponse getJourneySummary(final UUID lookupEntryId) {

    SearchDomain searchDomain = searchDomainRepo.findByLookupEntryId(lookupEntryId)
        .orElseThrow(() -> new RuntimeException("TODO: 404 No search domain record found"));

    log.debug("Found SearchDomain record: {}", searchDomain.toString());

    return new GetJourneySummaryResponse(searchDomain.getJourney().getId().toString(),
        searchDomain.getModifierJourneyName(), searchDomain.getJourneySelectionText(),
        searchDomain.getJourneySelectionDescription());
  }

  public List<SearchJourneyResponse> searchJourneys(final String searchTerm) {

    log.debug("Search journeys for searchTerm: '{}'", searchTerm);

    List<Object[]> searchDomains = searchDomainRepo.findBySearchTermFuzzyMatch(searchTerm);
    log.debug("Found {} matching SearchDomain records", searchDomains.size());

    return searchDomains.stream().map(sd -> {
      SearchJourneyResponse sjr = new SearchJourneyResponse();
      sjr.setJourneyId(String.valueOf(sd[0]));
      sjr.setModifier(sd[1] == null ? null : String.valueOf(sd[1]));
      sjr.setSelectionText(sd[2] == null ? null : String.valueOf(sd[2]));
      sjr.setSelectionDescription(sd[3] == null ? null : String.valueOf(sd[3]));
      return sjr;
    }).collect(Collectors.toList());

  }
}
