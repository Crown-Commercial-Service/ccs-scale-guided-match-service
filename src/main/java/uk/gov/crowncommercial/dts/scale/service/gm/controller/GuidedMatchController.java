package uk.gov.crowncommercial.dts.scale.service.gm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.gm.model.*;

/**
 * Guided Match Controller.
 *
 */
@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
@Slf4j
public class GuidedMatchController {

  @PostMapping("/journeys/{journey-id}")
  public StartJourneyResponse startJourney(@PathVariable("journey-id") final String journeyId,
      @RequestBody final JourneySelectionParameters journeySelectionParameters) {

    log.debug("startJourney(journey-id: {}, journeySelectionParameters: {})", journeyId,
        journeySelectionParameters);

    return new StartJourneyResponse(UUID.randomUUID().toString(), new HashSet<>());
  }

  @PostMapping("/journey-instances/{journey-instance-id}/questions/{question-id}")
  public GetJourneyQuestionOutcomeResponse getJourneyQuestionOutcome(
      @PathVariable("journey-instance-id") final String journeyInstanceId,
      @PathVariable("question-id") final String questionId,
      @RequestBody final Set<AnsweredQuestion> answeredQuestions) {

    log.debug(
        "getJourneyQuestionOutcome(journey-instance-id: {}, question-id: {}, answeredQuestions: {})",
        journeyInstanceId, questionId, answeredQuestions);

    return new GetJourneyQuestionOutcomeResponse(
        new Outcome(OutcomeType.SUPPORT, Instant.now(), null), new ArrayList<>());
  }

}
