package uk.gov.crowncommercial.dts.scale.service.gm.service;

import static uk.gov.crowncommercial.dts.scale.service.gm.model.OutcomeType.AGREEMENT;
import static uk.gov.crowncommercial.dts.scale.service.gm.model.OutcomeType.QUESTION;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.gm.config.DecisionTreeServiceConfig;
import uk.gov.crowncommercial.dts.scale.service.gm.exception.ResourceNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.gm.model.*;
import uk.gov.crowncommercial.dts.scale.service.gm.model.entity.JourneyInstance;
import uk.gov.crowncommercial.dts.scale.service.gm.model.external.dt.DTJourney;
import uk.gov.crowncommercial.dts.scale.service.gm.model.external.dt.DTOutcome;
import uk.gov.crowncommercial.dts.scale.service.gm.model.external.dt.DTQuestionDefinitionList;

/**
 * Service component to handle interaction with the Decision Tree service
 */
@Service
@Slf4j
public class DecisionTreeService {

  private final JourneyInstanceService journeyInstanceService;
  private final FailureValidationService failureValidationService;
  private final DecisionTreeServiceConfig decisionTreeServiceConfig;
  private final RestTemplate restTemplate;

  public DecisionTreeService(final JourneyInstanceService journeyInstanceService,
      final FailureValidationService failureValidationService,
      final RestTemplateBuilder restTemplateBuilder,
      final DecisionTreeServiceConfig decisionTreeServiceConfig) {

    this.journeyInstanceService = journeyInstanceService;
    this.failureValidationService = failureValidationService;
    this.decisionTreeServiceConfig = decisionTreeServiceConfig;
    this.restTemplate = restTemplateBuilder.rootUri(decisionTreeServiceConfig.getUrl()).build();
  }

  public QuestionDefinitionList getJourneyFirstQuestion(final String journeyId) {

    DTJourney dtJourney = restTemplate.getForObject(
        decisionTreeServiceConfig.getUriTemplates().getGetJourney(), DTJourney.class, journeyId);

    log.debug("Journey from Decision Tree service: {}", dtJourney);

    // TODO: post-MVP: DT service should support question groups (multiple questions)
    return convertDTQuestionDefinitionList(
        new DTQuestionDefinitionList(Arrays.asList(dtJourney.getFirstQuestion())));
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

    return convertDTQuestionDefinitionList(restTemplate.getForObject(
        decisionTreeServiceConfig.getUriTemplates().getGetJourneyQuestion(),
        DTQuestionDefinitionList.class, journeyInstance.getJourney().getId(), questionId));
  }

  private QuestionDefinitionList convertDTQuestionDefinitionList(
      final DTQuestionDefinitionList dtQuestionDefinitionList) {

    return new QuestionDefinitionList(
        dtQuestionDefinitionList.stream().map(dtQuestionDefinition -> {
          Question question = new Question(dtQuestionDefinition.getUuid(),
              dtQuestionDefinition.getText(), dtQuestionDefinition.getHint(),
              dtQuestionDefinition.getType(), dtQuestionDefinition.getUnit());

          return new QuestionDefinition(question, null, null, null,
              dtQuestionDefinition.getAnswerDefinitions(), failureValidationService
                  .findFailureValidationByQuestionDefId(dtQuestionDefinition.getDefinitionUuid()));
        }).collect(Collectors.toList()));
  }

  public Outcome getJourneyQuestionOutcome(final JourneyInstance journeyInstance,
      final String questionId, final Set<AnsweredQuestion> answeredQuestions) {

    Map<String, String> uriTemplateVars = new HashMap<>();
    uriTemplateVars.put("journey-uuid", journeyInstance.getJourney().getId().toString());
    uriTemplateVars.put("question-uuid", questionId);

    DTOutcome dtOutcome = restTemplate
        .postForEntity(decisionTreeServiceConfig.getUriTemplates().getGetJourneyQuestionOutcome(),
            answeredQuestions, DTOutcome.class, uriTemplateVars)
        .getBody();

    OutcomeData outcomeData = null;
    if (dtOutcome.getOutcomeType() == QUESTION) {
      outcomeData = convertDTQuestionDefinitionList((DTQuestionDefinitionList) dtOutcome.getData());
    } else if (dtOutcome.getOutcomeType() == AGREEMENT) {
      outcomeData = dtOutcome.getData();
    }

    return Outcome.builder().outcomeType(dtOutcome.getOutcomeType()).timestamp(Instant.now())
        .data(outcomeData).build();
  }

}
