package uk.gov.crowncommercial.dts.scale.service.gm.service;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner; 
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.rollbar.notifier.Rollbar;
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

  public static final int JOURNEY_ID_INDEX = 0;
  public static final int MODIFIER_JOURNEY_NAME_INDEX = 1;
  public static final int JOURNEY_SELECTION_TEXT_INDEX = 2;
  public static final int JOURNEY_SELECTION_DESC_INDEX = 3;

  private final SearchDomainRepo searchDomainRepo;

  @Autowired
  private Rollbar rollbar;
  
  public GetJourneySummaryResponse getJourneySummary(final UUID lookupEntryId) {

    SearchDomain searchDomain = searchDomainRepo.findByLookupEntryId(lookupEntryId)
        .orElseThrow(() -> new RuntimeException("TODO: 404 No search domain record found"));

    log.debug("Found SearchDomain record: {}", searchDomain.toString());

    return new GetJourneySummaryResponse(searchDomain.getJourney().getId().toString(),
        searchDomain.getModifierJourneyName(), searchDomain.getJourneySelectionText(),
        searchDomain.getJourneySelectionDescription());
  }

  public String checkStopword(String searchTerm) {

	  String orginalSearchTerm = searchTerm;

	  try {
		  ClassPathResource resource = new ClassPathResource("stopwords.txt");
		  BufferedReader stopwordFile = new BufferedReader(new InputStreamReader(resource.getInputStream()));

		  Scanner fileReader = new Scanner(stopwordFile);
		  while (fileReader.hasNextLine()) {
			  String stringFromFile = fileReader.nextLine().toLowerCase();

			  searchTerm = searchTerm.replaceAll(String.format("\\b%s\\b",stringFromFile), "").trim();
			  
			  if (searchTerm.isEmpty()) {
				  fileReader.close();
				  return orginalSearchTerm;
			  }
		  }
		  fileReader.close();
		  return searchTerm;
	  }catch (IOException e) {
		  log.error("Stopword file not found", e);
		  rollbar.error(e, "Stopword file not found");
	  }
	  return orginalSearchTerm;
  }

  public List<SearchJourneyResponse> searchJourneys(final String searchTerm) {

    log.debug("Search journeys for searchTerm: '{}'", checkStopword(searchTerm.toLowerCase()));
    List<Object[]> searchDomains = searchDomainRepo.findBySearchTermFuzzyMatch(checkStopword(searchTerm.toLowerCase()));
    log.debug("Found {} matching SearchDomain records", searchDomains.size());

    /**
     * Because of using the DISTINCT keyword in the native query, had to use an collection of Object
     * arrays as the return object, so need to map this to the SearchJourneyResponse.
     */
    return searchDomains.stream().map(sd -> {
      SearchJourneyResponse sjr = new SearchJourneyResponse();
      sjr.setJourneyId(String.valueOf(sd[JOURNEY_ID_INDEX]));
      sjr.setModifier(sd[MODIFIER_JOURNEY_NAME_INDEX] == null ? null
          : String.valueOf(sd[MODIFIER_JOURNEY_NAME_INDEX]));
      sjr.setSelectionText(sd[JOURNEY_SELECTION_TEXT_INDEX] == null ? null
          : String.valueOf(sd[JOURNEY_SELECTION_TEXT_INDEX]));
      sjr.setSelectionDescription(sd[JOURNEY_SELECTION_DESC_INDEX] == null ? null
          : String.valueOf(sd[JOURNEY_SELECTION_DESC_INDEX]));
      return sjr;
    }).collect(Collectors.toList());

  }
}
