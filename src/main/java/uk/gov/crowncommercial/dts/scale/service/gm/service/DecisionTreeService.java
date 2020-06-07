package uk.gov.crowncommercial.dts.scale.service.gm.service;

import java.util.Collections;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.crowncommercial.dts.scale.service.gm.model.*;
import uk.gov.crowncommercial.dts.scale.service.gm.model.external.dt.Journey;

/**
 * Service component to handle interaction with the Decision Tree service
 */
@Service
public class DecisionTreeService {

  private final RestTemplate restTemplate;
  private final String getJourneyUriTemplate;
  private final String getJourneyQuestionOutcomeUriTemplate;

  public DecisionTreeService(final RestTemplateBuilder restTemplateBuilder,
      @Value("${external.decision-tree-service.url}") final String dtServiceBaseUrl,
      @Value("${external.decision-tree-service.uri-templates.get-journey}") final String getJourneyUriTemplate,
      @Value("${external.decision-tree-service.uri-templates.get-journey-question-outcome}") final String getJourneyQuestionOutcomeUriTemplate) {

    this.restTemplate = restTemplateBuilder.rootUri(dtServiceBaseUrl).build();
    this.getJourneyUriTemplate = getJourneyUriTemplate;
    this.getJourneyQuestionOutcomeUriTemplate = getJourneyQuestionOutcomeUriTemplate;
  }

  public StartJourneyResponse startJourney(final String journeyId) {

    // TODO: Create a new Journey Instance in repo..

    Journey dtJourney = restTemplate.getForObject(getJourneyUriTemplate, Journey.class, journeyId);
    // TODO: Global handler for RestClientException

    // TODO: DT service should support question groups (multiple questions) embedded in the Journey
    Question question =
        new Question(dtJourney.getFirstQuestion().getUuid(), dtJourney.getFirstQuestion().getText(),
            dtJourney.getFirstQuestion().getHint(), dtJourney.getFirstQuestion().getType());

    QuestionDefinition questionDefinition = new QuestionDefinition(question, null, null, null,
        dtJourney.getFirstQuestion().getAnswerDefinitions());


    return new StartJourneyResponse("0", Collections.singleton(questionDefinition));
  }

  public GetJourneyQuestionOutcomeResponse getJourneyQuestionOutcome(final String journeyInstanceId,
      final String questionId, final Set<AnsweredQuestion> answeredQuestions) {

    // TODO: Get history from JourneyInstance repo?
    // TODO: Call DT service get question instance outcome
    return null;

  }

}
