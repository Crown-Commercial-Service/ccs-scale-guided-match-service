package uk.gov.crowncommercial.dts.scale.service.gm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import java.util.Set;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.gm.model.*;
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
  private final JourneyInstanceService guidedMatchHistoryService;

  @GetMapping("/journey-summaries/{lookup-entry-id}")
  public GetJourneySummaryResponse getJourneySummary(
      @PathVariable("lookup-entry-id") final UUID lookupEntryId) {

    log.debug("getJourneySummary(lookup-entry-id: {})", lookupEntryId);

    return searchTermLookupService.getJourneySummary(lookupEntryId);
  }

  @PostMapping(path = "/journeys/{journey-id}", consumes = APPLICATION_JSON_VALUE)
  public StartJourneyResponse startJourney(@PathVariable("journey-id") final String journeyId,
      @RequestBody final JourneySelectionParameters journeySelectionParameters) {

    log.debug("startJourney(journey-id: {}, journeySelectionParameters: {})", journeyId,
        journeySelectionParameters);

    return decisionTreeService.startJourney(journeyId, journeySelectionParameters);
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

    return decisionTreeService.getJourneyQuestionOutcome(journeyInstanceId, questionId,
        answeredQuestions);
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

    return guidedMatchHistoryService.getJourneyHistory(journeyInstanceId);
  }

}
