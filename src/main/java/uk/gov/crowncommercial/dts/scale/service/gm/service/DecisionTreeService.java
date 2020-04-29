package uk.gov.crowncommercial.dts.scale.service.gm.service;

import java.util.Set;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import uk.gov.crowncommercial.dts.scale.service.gm.model.AnsweredQuestion;
import uk.gov.crowncommercial.dts.scale.service.gm.model.GetJourneyQuestionOutcomeResponse;
import uk.gov.crowncommercial.dts.scale.service.gm.model.StartJourneyResponse;
import uk.gov.crowncommercial.dts.scale.service.gm.temp.DataLoader;

/**
 * Service component to handle interaction with the Decision Tree service
 */
@Service
@RequiredArgsConstructor
public class DecisionTreeService {

  private final DataLoader dataLoader;

  public StartJourneyResponse startJourney(final String journeyId) {

    // TODO: Create a new Journey Instance in repo..

    return dataLoader.convertJsonToObject("start-journey/" + journeyId + ".json",
        StartJourneyResponse.class);

  }

  public GetJourneyQuestionOutcomeResponse getJourneyQuestionOutcome(final String journeyInstanceId,
      final String questionId, final Set<AnsweredQuestion> answeredQuestions) {

    // TODO: Get history from JourneyInstance repo?
    String fileName =
        String.format("get-journey-question-outcome/%s-%s.json", journeyInstanceId, questionId);
    return dataLoader.convertJsonToObject(fileName, GetJourneyQuestionOutcomeResponse.class);

  }

}
