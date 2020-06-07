package uk.gov.crowncommercial.dts.scale.service.gm.service;

import java.time.LocalDate;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.crowncommercial.dts.scale.service.gm.model.*;
import uk.gov.crowncommercial.dts.scale.service.gm.model.entity.JourneyInstance;
import uk.gov.crowncommercial.dts.scale.service.gm.model.external.dt.DTJourney;
import uk.gov.crowncommercial.dts.scale.service.gm.repository.JourneyInstanceRepo;
import uk.gov.crowncommercial.dts.scale.service.gm.repository.JourneyRepo;

/**
 * Service component to handle interaction with the Decision Tree service
 */
@Service
public class DecisionTreeService {

  private final JourneyInstanceRepo journeyInstanceRepo;
  private final JourneyRepo journeyRepo;

  private final RestTemplate restTemplate;
  private final String getJourneyUriTemplate;
  private final String getJourneyQuestionOutcomeUriTemplate;

  public DecisionTreeService(final JourneyInstanceRepo journeyInstanceRepo,
      final JourneyRepo journeyRepo, final RestTemplateBuilder restTemplateBuilder,
      @Value("${external.decision-tree-service.url}") final String dtServiceBaseUrl,
      @Value("${external.decision-tree-service.uri-templates.get-journey}") final String getJourneyUriTemplate,
      @Value("${external.decision-tree-service.uri-templates.get-journey-question-outcome}") final String getJourneyQuestionOutcomeUriTemplate) {

    this.journeyInstanceRepo = journeyInstanceRepo;
    this.journeyRepo = journeyRepo;
    this.restTemplate = restTemplateBuilder.rootUri(dtServiceBaseUrl).build();
    this.getJourneyUriTemplate = getJourneyUriTemplate;
    this.getJourneyQuestionOutcomeUriTemplate = getJourneyQuestionOutcomeUriTemplate;
  }

  public StartJourneyResponse startJourney(final String journeyId) {

    // TODO: Create a new Journey Instance in repo..
    DTJourney dtJourney =
        restTemplate.getForObject(getJourneyUriTemplate, DTJourney.class, journeyId);
    JourneyInstance journeyInstance = new JourneyInstance();

    journeyInstance.setJourney(journeyRepo.findById(UUID.fromString(dtJourney.getUuid())).get());
    journeyInstance.setStartDate(LocalDate.now());
    journeyInstanceRepo.saveAndFlush(journeyInstance);

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

    // TODO: Get history from JourneyInstance repo
    JourneyInstance journeyInstance = journeyInstanceRepo.findById(Long.valueOf(journeyInstanceId))
        .orElseThrow(() -> new RuntimeException("TODO: 404 Journey Instance record not found"));

    // TODO: Call DT service get question instance outcome
    Map<String, String> uriTemplateVars = new HashMap<>();
    uriTemplateVars.put("journey-uuid", journeyInstance.getJourney().getId().toString());
    uriTemplateVars.put("question-uuid", questionId);

    // restTemplate.postForEntity(getJourneyQuestionOutcomeUriTemplate, answeredQuestions, Outcome,
    // uriTemplateVars);

    return null;

  }

}
