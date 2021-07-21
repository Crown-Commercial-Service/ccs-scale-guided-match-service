package uk.gov.crowncommercial.dts.scale.service.gm.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.rollbar.notifier.Rollbar;

import uk.gov.crowncommercial.dts.scale.service.gm.model.SearchJourneyResponse;
import uk.gov.crowncommercial.dts.scale.service.gm.service.DecisionTreeService;
import uk.gov.crowncommercial.dts.scale.service.gm.service.JourneyInstanceService;
import uk.gov.crowncommercial.dts.scale.service.gm.service.SearchTermLookupService;

@WebMvcTest(GuidedMatchController.class)
public class GuidedMatchControllerTest {

  private static final String SEARCH_TERM = "linen";
  private static final String JOURNEY_ID = "journey_id";

  @Autowired
  private MockMvc mockMvc;

  @MockBean 
  private Rollbar rollbar;
  
  @MockBean
  private SearchTermLookupService service;

  @MockBean
  private DecisionTreeService dtService;

  @MockBean
  private JourneyInstanceService journeyInstanceService;

  @Test
  public void testGetAgreementSummariesSuccess() throws Exception {

    SearchJourneyResponse response = new SearchJourneyResponse();
    response.setJourneyId(JOURNEY_ID);

    List<SearchJourneyResponse> responses = new ArrayList<>();
    responses.add(response);

    when(service.searchJourneys(SEARCH_TERM)).thenReturn(responses);
    this.mockMvc.perform(get("/search-journeys/linen")).andExpect(status().isOk())
        .andExpect(content().string(containsString(JOURNEY_ID)));
  }

}
