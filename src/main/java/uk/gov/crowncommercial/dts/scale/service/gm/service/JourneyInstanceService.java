package uk.gov.crowncommercial.dts.scale.service.gm.service;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import javax.transaction.Transactional;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.gm.exception.MissingGMDataException;
import uk.gov.crowncommercial.dts.scale.service.gm.exception.ResourceNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.gm.model.*;
import uk.gov.crowncommercial.dts.scale.service.gm.model.entity.*;
import uk.gov.crowncommercial.dts.scale.service.gm.repository.JourneyInstanceQuestionRepo;
import uk.gov.crowncommercial.dts.scale.service.gm.repository.JourneyInstanceRepo;
import uk.gov.crowncommercial.dts.scale.service.gm.repository.JourneyRepo;

/**
 * Service component handling all operations around journey instances, questions, answers and
 * outcomes.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JourneyInstanceService {

  private final JourneyInstanceRepo journeyInstanceRepo;
  private final JourneyInstanceQuestionRepo journeyInstanceQuestionRepo;
  private final JourneyRepo journeyRepo;
  private final Clock clock;

  public JourneyInstance createJourneyInstance(final String journeyId,
      final String originalSearchTerm) {

    Journey journey = journeyRepo.findById(UUID.fromString(journeyId))
        .orElseThrow(() -> new MissingGMDataException("Journey not found in repo: " + journeyId));

    JourneyInstance journeyInstance = new JourneyInstance();
    journeyInstance.setUuid(UUID.randomUUID());
    journeyInstance.setJourney(journey);
    journeyInstance.setStartDateTime(LocalDateTime.now(clock));
    journeyInstance.setOriginalSearchTerm(originalSearchTerm);

    return journeyInstanceRepo.saveAndFlush(journeyInstance);
  }

  public Optional<JourneyInstance> findByUuid(final UUID uuid) {
    return journeyInstanceRepo.findByUuid(uuid);
  }

  /**
   * Configures and persists a new journey instance question
   *
   * @param journeyInstance
   * @param questionInstance
   */
  public void updateJourneyInstanceQuestions(final JourneyInstance journeyInstance,
      final Question questionInstance) {

    // Find the JIQ that now has the highest order and +1 to it.
    Optional<JourneyInstanceQuestion> jiqHighestOrder =
        journeyInstanceQuestionRepo.findTopByJourneyInstanceOrderByOrderDesc(journeyInstance);

    final short order;
    if (jiqHighestOrder.isPresent()) {
      order = (short) (jiqHighestOrder.get().getOrder() + 1);
    } else {
      order = 1;
    }

    JourneyInstanceQuestion jiq = new JourneyInstanceQuestion();
    jiq.setUuid(UUID.fromString(questionInstance.getId()));
    jiq.setText(questionInstance.getText());
    jiq.setHint(questionInstance.getHint());
    jiq.setType(questionInstance.getType());
    jiq.setOrder(order);
    jiq.setUnit(questionInstance.getUnit());
    journeyInstance.addJourneyInstanceQuestion(jiq);

    journeyInstanceRepo.saveAndFlush(journeyInstance);
  }

  /**
   * Clears the journey history from <code>questionId</code> onwards based on order.
   *
   * @param journeyInstanceId
   * @param questionId
   */
  @Transactional
  public void clearJourneyInstanceHistory(final String journeyInstanceId, final String questionId) {
    JourneyInstance journeyInstance =
        journeyInstanceRepo.findByUuid(UUID.fromString(journeyInstanceId))
            .orElseThrow(() -> new MissingGMDataException(
                "No JourneyInstance record found for: " + journeyInstanceId));

    journeyInstance.getJourneyInstanceQuestions().stream()
        .filter(jiq -> jiq.getUuid().equals(UUID.fromString(questionId))).findFirst()
        .ifPresent(currentJiq -> {

          // Delete all instances from repo with >= order than the current instance..
          log.debug("About to delete JIQs by JourneyInstance and >= order: {}",
              currentJiq.getOrder());
          journeyInstanceQuestionRepo.deleteByJourneyInstanceAndOrderIsGreaterThanEqual(
              journeyInstance, currentJiq.getOrder());
        });
  }

  /**
   * Updates the journey instance answers for a journey instance question
   *
   * @param journeyInstance
   * @param answeredQuestions
   * @param answeredQuestionDefinition
   */
  public void updateJourneyInstanceAnswers(final JourneyInstance journeyInstance,
      final Set<AnsweredQuestion> answeredQuestions,
      final QuestionDefinition answeredQuestionDefinition) {

    answeredQuestions.stream().forEach(aq -> {

      JourneyInstanceQuestion journeyInstanceQuestion =
          journeyInstance.getJourneyInstanceQuestions().stream()
              .filter(jiq -> jiq.getUuid().toString().equals(aq.getUuid())).findFirst()
              .orElseThrow(() -> new MissingGMDataException(
                  "Journey instance question matching answered question not found in repo: "
                      + aq.getUuid()));

      aq.getAnswers().stream().forEach(a -> {
        JourneyInstanceAnswer jia = new JourneyInstanceAnswer();

        AnswerDefinition answerDef = answeredQuestionDefinition.getAnswerDefinitions().stream()
            .filter(ad -> ad.getId().equals(a.getUuid())).findFirst().get();

        jia.setAnswerText(answerDef.getText());
        jia.setAnswerId(UUID.fromString(a.getUuid()));

        if (answerDef.getConditionalInput() != null) {
          jia.setUnit(answerDef.getConditionalInput().getUnit());
        }

        if (NumberUtils.isCreatable(a.getValue())) {
          jia.setValueNumber(new BigDecimal(a.getValue()));
        }
        journeyInstanceQuestion.addJourneyInstanceAnswer(jia);
      });

    });
    journeyInstanceRepo.saveAndFlush(journeyInstance);
  }

  /**
   * Conditionally update the given <code>JourneyInstance</code> with outcome details if outcome has
   * type {@link OutcomeType#AGREEMENT} or {@link OutcomeType#SUPPORT}, otherwise do nothing.
   *
   * @param journeyInstance
   * @param outcome
   */
  public void updateJourneyInstanceOutcome(final JourneyInstance journeyInstance,
      final Outcome outcome) {

    if (outcome.getOutcomeType() == OutcomeType.AGREEMENT
        || outcome.getOutcomeType() == OutcomeType.SUPPORT) {

      log.debug("Outcome is of type AGREEMENT or SUPPORT, updating journey instance");

      journeyInstance.setEndDateTime(LocalDateTime.now(clock));
      journeyInstance.setOutcomeType(outcome.getOutcomeType());
      journeyInstance.clearJourneyInstanceOutcomeDetails();

      final JourneyInstance updatedJourneyInstance =
          journeyInstanceRepo.saveAndFlush(journeyInstance);

      if (outcome.getOutcomeType() == OutcomeType.AGREEMENT) {

        log.debug(
            "Outcome is of type AGREEMENT, updating journey instance with agreement and lot details");

        AgreementList agreementList = (AgreementList) outcome.getData();

        agreementList.stream().forEach(agreement -> {

          if (CollectionUtils.isEmpty(agreement.getLots())) {
            updatedJourneyInstance.addJourneyInstanceOutcomeDetails(
                createJourneyInstanceOutcomeDetails(agreement.getNumber(), Optional.empty()));
          } else {
            agreement.getLots().stream()
                .forEach(lot -> updatedJourneyInstance.addJourneyInstanceOutcomeDetails(
                    createJourneyInstanceOutcomeDetails(agreement.getNumber(),
                        Optional.of(lot.getNumber()))));
          }
        });
      }
      journeyInstanceRepo.saveAndFlush(updatedJourneyInstance);
    }
  }

  private JourneyInstanceOutcomeDetails createJourneyInstanceOutcomeDetails(
      final String agreementNumber, final Optional<String> lotNumber) {
    JourneyInstanceOutcomeDetails jiod = new JourneyInstanceOutcomeDetails();
    jiod.setAgreementNumber(agreementNumber);
    if (lotNumber.isPresent()) {
      jiod.setLotNumber(lotNumber.get());
    }
    return jiod;
  }

  public GetJourneyHistoryResponse getJourneyHistory(final String journeyInstanceId) {
    JourneyInstance journeyInstance = findByUuid(UUID.fromString(journeyInstanceId)).orElseThrow(
        () -> new ResourceNotFoundException("Journey instance not found: " + journeyInstanceId));

    OutcomeData outcomeData = null;
    if (journeyInstance.getOutcomeType() == OutcomeType.AGREEMENT) {
      final Set<Agreement> agreements = new HashSet<>();
      final Map<String, Set<Lot>> agreementLots = new HashMap<>();

      // Map the 'flat' database structure (with repeating agreement number) to the Agreement model
      journeyInstance.getJourneyInstanceOutcomeDetails().stream().forEach(jiod -> {
        agreementLots.computeIfAbsent(jiod.getAgreementNumber(), k -> new HashSet<>());

        if (isNotBlank(jiod.getLotNumber())) {
          Lot lot = Lot.builder().number(jiod.getLotNumber()).build();
          agreementLots.get(jiod.getAgreementNumber()).add(lot);
        }
      });
      agreementLots.entrySet().stream().forEach(agreementLot -> agreements
          .add(new Agreement(agreementLot.getKey(), agreementLot.getValue())));

      outcomeData = new AgreementList(agreements);
    }

    return new GetJourneyHistoryResponse(journeyInstance.getOriginalSearchTerm(), null,
        Outcome.builder().outcomeType(journeyInstance.getOutcomeType()).data(outcomeData)
            .timestamp(journeyInstance.getEndDateTime().toInstant(ZoneOffset.UTC)).build(),
        getQuestionHistory(journeyInstance));
  }

  public Set<QuestionHistory> getQuestionHistory(final String journeyInstanceId) {
    JourneyInstance journeyInstance = findByUuid(UUID.fromString(journeyInstanceId)).orElseThrow(
        () -> new ResourceNotFoundException("Journey instance not found: " + journeyInstanceId));
    return getQuestionHistory(journeyInstance);
  }

  public Set<QuestionHistory> getQuestionHistory(final JourneyInstance journeyInstance) {
    final Set<QuestionHistory> questionHistory = new LinkedHashSet<>();

    if (!journeyInstance.getJourneyInstanceQuestions().isEmpty()) {
      journeyInstance.getJourneyInstanceQuestions().stream()
          .sorted(Comparator.comparing(JourneyInstanceQuestion::getOrder))
          .filter(jiq -> !jiq.getJourneyInstanceAnswers().isEmpty()).forEach(jiq -> {
            Question question = new Question(jiq.getUuid().toString(), jiq.getText(), jiq.getHint(),
                jiq.getType(), jiq.getUnit());
            Set<AnswerHistory> answers = new HashSet<>();
            jiq.getJourneyInstanceAnswers().stream().forEach(jia -> {

              String answerHistoryValue = jia.getValueNumber() != null
                  ? jia.getValueNumber().toString() : jia.getAnswerId().toString();

              answers
                  .add(new AnswerHistory(jia.getAnswerText(), answerHistoryValue, jia.getUnit()));
            });
            questionHistory.add(new QuestionHistory(question, answers, ""));
          });
    }
    return questionHistory;
  }

}
