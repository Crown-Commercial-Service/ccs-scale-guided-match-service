package uk.gov.crowncommercial.dts.scale.service.gm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.gm.exception.ResourceNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.gm.model.*;
import uk.gov.crowncommercial.dts.scale.service.gm.model.entity.JourneyInstance;
import uk.gov.crowncommercial.dts.scale.service.gm.service.DecisionTreeService;
import uk.gov.crowncommercial.dts.scale.service.gm.service.JourneyInstanceService;
import uk.gov.crowncommercial.dts.scale.service.gm.service.SearchTermLookupService;

/**
 * Guided Match Controller.
 *
 */
@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class GuidedMatchController {

  private final SearchTermLookupService searchTermLookupService;
  private final DecisionTreeService decisionTreeService;
  private final JourneyInstanceService journeyInstanceService;

  @PostMapping(path = "/journeys/{journey-id}", consumes = APPLICATION_JSON_VALUE)
  public StartJourneyResponse startJourney(@PathVariable("journey-id") final String journeyId,
      @RequestBody final JourneySelectionParameters journeySelectionParameters) {

    log.debug("startJourney(journey-id: {}, journeySelectionParameters: {})", journeyId,
        journeySelectionParameters);

    // Start a 'session' by creating and persisting a new Journey Instance
    JourneyInstance journeyInstance = journeyInstanceService.createJourneyInstance(journeyId,
        journeySelectionParameters.getSearchTerm(),journeySelectionParameters.getSelectedDomain());

    // Get the journey first question definition
    QuestionDefinitionList questionDefinitionList =
        decisionTreeService.getJourneyFirstQuestion(journeyId);

    return new StartJourneyResponse(journeyInstance.getUuid().toString(), questionDefinitionList);
  }

  @PostMapping(path = "/journey-instances/{journey-instance-id}/questions/{question-id}",
      consumes = APPLICATION_JSON_VALUE)
  public GetJourneyQuestionOutcomeResponse getJourneyQuestionOutcome(
      @PathVariable("journey-instance-id") final String journeyInstanceId,
      @PathVariable("question-id") final String questionId,
      @RequestBody final Set<AnsweredQuestion> answeredQuestions) {

    log.debug(
        "getJourneyQuestionOutcome(journey-instance-id: {}, question-id: {}, answeredQuestions: {})",
        journeyInstanceId, questionId, answeredQuestions);

    // Get the journey instance and update with the current question and answers
    JourneyInstance journeyInstance =
        journeyInstanceService.findByUuid(UUID.fromString(journeyInstanceId))
            .orElseThrow(() -> new ResourceNotFoundException(
                "Journey instance not found: " + journeyInstanceId));

    journeyInstanceService.clearJourneyInstanceHistory(journeyInstanceId, questionId);

    QuestionDefinition questionDefinition =
        decisionTreeService.getJourneyQuestion(journeyInstanceId, questionId).get(0);

    journeyInstanceService.updateJourneyInstanceQuestions(journeyInstance,
        questionDefinition.getQuestion());
    journeyInstanceService.updateJourneyInstanceAnswers(journeyInstance, answeredQuestions,
        questionDefinition);

    // Get the question outcome from the decision tree service and update the journey instance
    // outcome
    Outcome outcome = decisionTreeService.getJourneyQuestionOutcome(journeyInstance, questionId,
        answeredQuestions);

    journeyInstanceService.updateJourneyInstanceOutcome(journeyInstance, outcome);

    Set<QuestionHistory> journeyHistory =
        journeyInstanceService.getQuestionHistory(journeyInstanceId);

    return new GetJourneyQuestionOutcomeResponse(outcome, journeyHistory);
  }

  @GetMapping(path = "/journey-instances/{journey-instance-id}/questions/{question-id}")
  public QuestionDefinitionList getJourneyQuestion(
      @PathVariable("journey-instance-id") final String journeyInstanceId,
      @PathVariable("question-id") final String questionId) {

    log.debug("getJourneyQuestion(journey-instance-id: {}, question-id: {})", journeyInstanceId,
        questionId);

    return decisionTreeService.getJourneyQuestion(journeyInstanceId, questionId);
  }

  @GetMapping("/journey-instances/{journey-instance-id}")
  public GetJourneyHistoryResponse getJourneyHistory(
      @PathVariable("journey-instance-id") final String journeyInstanceId) {

    log.debug("getJourneyHistory(journey-instance-id: {})", journeyInstanceId);

    return journeyInstanceService.getJourneyHistory(journeyInstanceId);
  }

  @GetMapping("/search-journeys/{search-term}")
  public List<SearchJourneyResponse> searchJourneys(
      @PathVariable("search-term") final String searchTerm) {

    log.debug("searchJourneys(search-term: {})", searchTerm);

    List<SearchJourneyResponse>  searchTermResult = searchTermLookupService.searchJourneys(searchTerm);
    
    if (searchTermResult.isEmpty()) {
        log.debug("Using GM Lite Journeys");

    	SearchJourneyResponse sjr = new SearchJourneyResponse();
        sjr.setJourneyId("c9dd4455-7d23-4822-9912-eab4da9fc5a2");
        
        searchTermResult.add(sjr);
    }
    
    return searchTermResult;
  }

}
