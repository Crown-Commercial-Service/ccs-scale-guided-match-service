package uk.gov.crowncommercial.dts.scale.service.gm.service;

import static uk.gov.crowncommercial.dts.scale.service.gm.model.OutcomeType.AGREEMENT;
import static uk.gov.crowncommercial.dts.scale.service.gm.model.OutcomeType.QUESTION;
import static uk.gov.crowncommercial.dts.scale.service.gm.model.OutcomeType.SUPPORT;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.gm.exception.MissingGMDataException;
import uk.gov.crowncommercial.dts.scale.service.gm.exception.ResourceNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.gm.model.*;
import uk.gov.crowncommercial.dts.scale.service.gm.model.entity.JourneyInstance;
import uk.gov.crowncommercial.dts.scale.service.gm.model.external.dt.DTJourney;
import uk.gov.crowncommercial.dts.scale.service.gm.model.external.dt.DTOutcome;
import uk.gov.crowncommercial.dts.scale.service.gm.model.external.dt.DTQuestionDefinitionList;
import uk.gov.crowncommercial.dts.scale.service.gm.repository.JourneyRepo;

/**
 * Service component to handle interaction with the Decision Tree service
 */
@Service
@Slf4j
public class DecisionTreeService {

  private final JourneyInstanceService journeyInstanceService;
  private final JourneyRepo journeyRepo;

  private final RestTemplate restTemplate;
  private final String getJourneyUriTemplate;
  private final String getJourneyQuestionOutcomeUriTemplate;
  private final String getJourneyQuestionUriTemplate;

  public DecisionTreeService(final JourneyInstanceService journeyInstanceService,
      final JourneyRepo journeyRepo, final RestTemplateBuilder restTemplateBuilder,
      @Value("${external.decision-tree-service.url}") final String dtServiceBaseUrl,
      @Value("${external.decision-tree-service.uri-templates.get-journey}") final String getJourneyUriTemplate,
      @Value("${external.decision-tree-service.uri-templates.get-journey-question-outcome}") final String getJourneyQuestionOutcomeUriTemplate,
      @Value("${external.decision-tree-service.uri-templates.get-journey-question}") final String getJourneyQuestionUriTemplate) {

    this.journeyInstanceService = journeyInstanceService;
    this.journeyRepo = journeyRepo;
    this.restTemplate = restTemplateBuilder.rootUri(dtServiceBaseUrl).build();
    this.getJourneyUriTemplate = getJourneyUriTemplate;
    this.getJourneyQuestionOutcomeUriTemplate = getJourneyQuestionOutcomeUriTemplate;
    this.getJourneyQuestionUriTemplate = getJourneyQuestionUriTemplate;
  }

  public StartJourneyResponse startJourney(final String journeyId,
      final JourneySelectionParameters journeySelectionParameters) {

    // Start a 'session' by creating and persisting a new Journey Instance
    DTJourney dtJourney =
        restTemplate.getForObject(getJourneyUriTemplate, DTJourney.class, journeyId);

    log.debug("Journey from Decision Tree service: {}", dtJourney);
    UUID journeyUuid = UUID.fromString(dtJourney.getUuid());
    JourneyInstance journeyInstance = journeyInstanceService.createJourneyInstance(
        journeyRepo.findById(journeyUuid).orElseThrow(
            () -> new MissingGMDataException("Journey not found in repo: " + journeyUuid)),
        journeySelectionParameters.getSearchTerm());

    // TODO: post-MVP: DT service should support question groups (multiple questions)
    QuestionDefinitionList questionDefinitionList = convertDTQuestionDefinitionList(
        new DTQuestionDefinitionList(Arrays.asList(dtJourney.getFirstQuestion())));

    // Update journey history with details of the first question:
    journeyInstanceService.updateJourneyInstanceQuestions(journeyInstance, questionDefinitionList);

    return new StartJourneyResponse(journeyInstance.getUuid().toString(), questionDefinitionList);
  }

  /**
   * Used in navigation (i.e. back to previous questions)
   *
   * @param journeyInstanceId
   * @param questionId
   * @return the requested question definition (list)
   */
  public QuestionDefinitionList getJourneyQuestion(final String journeyInstanceId,
      final String questionId) {
    JourneyInstance journeyInstance =
        journeyInstanceService.findByUuid(UUID.fromString(journeyInstanceId))
            .orElseThrow(() -> new ResourceNotFoundException(
                "Journey instance not found: " + journeyInstanceId));

    QuestionDefinitionList questionDefinitionList =
        convertDTQuestionDefinitionList(restTemplate.getForObject(getJourneyQuestionUriTemplate,
            DTQuestionDefinitionList.class, journeyInstance.getJourney().getId(), questionId));

    journeyInstanceService.updateJourneyInstanceQuestions(journeyInstance, questionDefinitionList);

    return questionDefinitionList;
  }

  private QuestionDefinitionList convertDTQuestionDefinitionList(
      final DTQuestionDefinitionList dtQuestionDefinitionList) {

    return new QuestionDefinitionList(
        dtQuestionDefinitionList.stream().map(dtQuestionDefinition -> {
          Question question = new Question(dtQuestionDefinition.getUuid(),
              dtQuestionDefinition.getText(), dtQuestionDefinition.getHint(),
              dtQuestionDefinition.getType(), dtQuestionDefinition.getUnit());

          return new QuestionDefinition(question, null, null, null,
              dtQuestionDefinition.getAnswerDefinitions());
        }).collect(Collectors.toList()));
  }

  public Outcome getJourneyQuestionOutcome(final String journeyInstanceId, final String questionId,
      final Set<AnsweredQuestion> answeredQuestions) {

    JourneyInstance journeyInstance =
        journeyInstanceService.findByUuid(UUID.fromString(journeyInstanceId))
            .orElseThrow(() -> new ResourceNotFoundException(
                "Journey instance not found: " + journeyInstanceId));

    QuestionDefinition answeredQuestionDefinition =
        convertDTQuestionDefinitionList(restTemplate.getForObject(getJourneyQuestionUriTemplate,
            DTQuestionDefinitionList.class, journeyInstance.getJourney().getId(), questionId))
                .get(0);

    journeyInstanceService.updateJourneyInstanceAnswers(journeyInstance, answeredQuestions,
        answeredQuestionDefinition);

    Map<String, String> uriTemplateVars = new HashMap<>();
    uriTemplateVars.put("journey-uuid", journeyInstance.getJourney().getId().toString());
    uriTemplateVars.put("question-uuid", questionId);

    DTOutcome dtOutcome = restTemplate.postForEntity(getJourneyQuestionOutcomeUriTemplate,
        answeredQuestions, DTOutcome.class, uriTemplateVars).getBody();

    OutcomeData outcomeData = null;
    if (dtOutcome.getOutcomeType() == QUESTION) {
      QuestionDefinitionList questionDefinitionList =
          convertDTQuestionDefinitionList((DTQuestionDefinitionList) dtOutcome.getData());
      outcomeData = questionDefinitionList;

      // Update journey history with details of the next question(s)
      journeyInstanceService.updateJourneyInstanceQuestions(journeyInstance,
          questionDefinitionList);
    } else if (dtOutcome.getOutcomeType() == AGREEMENT) {
      outcomeData = dtOutcome.getData();
      journeyInstanceService.updateJourneyInstanceOutcome(journeyInstance, AGREEMENT,
          Optional.of(outcomeData));
    } else if (dtOutcome.getOutcomeType() == SUPPORT) {
      journeyInstanceService.updateJourneyInstanceOutcome(journeyInstance, SUPPORT,
          Optional.empty());
    }

    return Outcome.builder().outcomeType(dtOutcome.getOutcomeType()).timestamp(Instant.now())
        .data(outcomeData).build();
  }

}
