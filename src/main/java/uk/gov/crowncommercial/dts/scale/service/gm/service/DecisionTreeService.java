package uk.gov.crowncommercial.dts.scale.service.gm.service;

import java.util.Set;
import java.util.StringJoiner;
import org.apache.commons.lang3.StringUtils;
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

    // First 8 digits of the answer UUIDs is sufficient
    final StringJoiner sj = new StringJoiner(",");
    answeredQuestions.stream().findFirst().get().getAnswers().stream()
        .map(a -> StringUtils.substringBefore(a, "-")).sorted().forEach(sj::add);

    // TODO: Get history from JourneyInstance repo?
    String fileName = String.format("get-journey-question-outcome/%s_%s_(%s).json",
        journeyInstanceId, questionId, sj.toString());
    return dataLoader.convertJsonToObject(fileName, GetJourneyQuestionOutcomeResponse.class);

  }

}
