package uk.gov.crowncommercial.dts.scale.service.gm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.gm.model.SearchJourneyResponse;
import uk.gov.crowncommercial.dts.scale.service.gm.repository.SearchDomainRepo;

@SpringBootTest
@ActiveProfiles("test")
public class SearchTermLookupServiceTest {

  private static final String SEARCH_TERM = "linen";
  private static final String JOURNEY_ID = "journey_id";
  private static final String MODIFIER_JOURNEY_NAME = "modifier";
  private static final String JOURNEY_SELECTION_TEXT = "selection text";
  private static final String JOURNEY_SELECTION_DESC = "selection desc";

  @MockBean
  private SearchDomainRepo mockSearchDomainRepo;

  @Autowired
  SearchTermLookupService service;

  @Test
  public void testSearchJourney() throws Exception {
    when(mockSearchDomainRepo.findBySearchTermFuzzyMatch(SEARCH_TERM)).thenReturn(getDbSearchMatch(
        JOURNEY_ID, MODIFIER_JOURNEY_NAME, JOURNEY_SELECTION_TEXT, JOURNEY_SELECTION_DESC));
    List<SearchJourneyResponse> response = service.searchJourneys(SEARCH_TERM);
    assertEquals(JOURNEY_ID, response.get(0).getJourneyId());
    assertEquals(MODIFIER_JOURNEY_NAME, response.get(0).getModifier());
    assertEquals(JOURNEY_SELECTION_TEXT, response.get(0).getSelectionText());
    assertEquals(JOURNEY_SELECTION_DESC, response.get(0).getSelectionDescription());
  }

  @Test
  public void testSearchJourneyWithNulls() throws Exception {
    when(mockSearchDomainRepo.findBySearchTermFuzzyMatch(SEARCH_TERM))
        .thenReturn(getDbSearchMatch(JOURNEY_ID, null, null, null));
    List<SearchJourneyResponse> response = service.searchJourneys(SEARCH_TERM);
    assertEquals(JOURNEY_ID, response.get(0).getJourneyId());
    assertEquals(null, response.get(0).getModifier());
    assertEquals(null, response.get(0).getSelectionText());
    assertEquals(null, response.get(0).getSelectionDescription());
  }

  private List<Object[]> getDbSearchMatch(String journeyId, String modifier, String text,
      String desc) {
    Object[] match = {journeyId, modifier, text, desc};
    List<Object[]> response = new ArrayList<>();
    response.add(match);
    return response;
  }
}
