package uk.gov.crowncommercial.dts.scale.service.gm.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import uk.gov.crowncommercial.dts.scale.service.gm.model.*;
import uk.gov.crowncommercial.dts.scale.service.gm.model.entity.*;
import uk.gov.crowncommercial.dts.scale.service.gm.repository.JourneyInstanceQuestionRepo;
import uk.gov.crowncommercial.dts.scale.service.gm.repository.JourneyInstanceRepo;
import uk.gov.crowncommercial.dts.scale.service.gm.temp.DataLoader;

/**
 *
 */
@Service
@RequiredArgsConstructor
public class JourneyInstanceService {

  private final JourneyInstanceRepo journeyInstanceRepo;
  private final JourneyInstanceQuestionRepo journeyInstanceQuestionRepo;
  private final DataLoader dataLoader;
  private final Clock clock;

  public GetJourneyHistoryResponse getJourneyHistory(final String journeyInstanceId) {
    // TODO: Implement once data schema issues resolved
    return dataLoader.convertJsonToObject("get-journey-history/" + journeyInstanceId + ".json",
        GetJourneyHistoryResponse.class);
  }

  public JourneyInstance createJourneyInstance(final Journey journey,
      final String originalSearchTerm) {

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

  public void updateJourneyInstanceAnswers(final JourneyInstance journeyInstance,
      final Set<AnsweredQuestion> answeredQuestions) {

    answeredQuestions.stream().forEach(aq -> {

      JourneyInstanceQuestion journeyInstanceQuestion =
          journeyInstance.getJourneyInstanceQuestions().stream()
              .filter(jiq -> jiq.getUuid().toString().equals(aq.getUuid())).findFirst()
              .orElseThrow(() -> new RuntimeException("TODO: 404 etc"));

      aq.getAnswers().stream().forEach(a -> {
        JourneyInstanceAnswer jia = new JourneyInstanceAnswer();
        jia.setJourneyInstanceQuestionId(journeyInstanceQuestion.getId());
        jia.setAnswerId(UUID.fromString(a.getUuid()));
        jia.setAnswerText(a.getValue());

        // TODO: Capture date/number value answers into separate fields or remove those columns
        journeyInstanceQuestion.getJourneyInstanceAnswers().add(jia);
      });

    });
    journeyInstanceRepo.saveAndFlush(journeyInstance);
  }

  /**
   * For MVP, we are recording a single linear journey only, so when a client returns to a previous
   * question (or the start of the journey) we must overwrite any question history from that point
   * on.
   *
   * @param journeyInstance
   * @param questionDefinitionList
   */
  public void updateJourneyInstanceQuestions(JourneyInstance journeyInstance,
      final QuestionDefinitionList questionDefinitionList) {

    // TODO: Post-MVP: Deal with question groups (the whole collection)
    final Question questionInstance =
        questionDefinitionList.stream().findFirst().get().getQuestion();

    // Delete all instances from repo with >= order than the current instance..
    Optional<JourneyInstanceQuestion> currentJiq = journeyInstanceQuestionRepo
        .findByJourneyInstanceAndUuid(journeyInstance, UUID.fromString(questionInstance.getId()));
    if (currentJiq.isPresent()) {

      journeyInstanceQuestionRepo.deleteByJourneyInstanceAndOrder(journeyInstance,
          currentJiq.get().getOrder());
      journeyInstanceQuestionRepo.flush();
      journeyInstance = journeyInstanceRepo.findById(journeyInstance.getId()).get();
    }

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
    journeyInstance.addJourneyInstanceQuestion(jiq);

    journeyInstanceRepo.saveAndFlush(journeyInstance);
  }

  public void updateJourneyInstanceOutcome(final JourneyInstance journeyInstance,
      final OutcomeType outcomeType, final Optional<OutcomeData> outcomeData) {

    journeyInstance.setEndDateTime(LocalDateTime.now(clock));
    journeyInstance.setOutcomeType(outcomeType);
    journeyInstance.clearJourneyInstanceOutcomeDetails();

    final JourneyInstance updatedJourneyInstance =
        journeyInstanceRepo.saveAndFlush(journeyInstance);

    if (outcomeType == OutcomeType.AGREEMENT) {
      AgreementList agreementList = (AgreementList) outcomeData
          .orElseThrow(() -> new RuntimeException("TODO: 500 Agreement outcome without agreement"));

      agreementList.stream().forEach(agreement -> {

        if (agreement.getLots().isEmpty()) {
          updatedJourneyInstance.addJourneyInstanceOutcomeDetails(
              createJourneyInstanceOutcomeDetails(agreement.getNumber(), Optional.empty()));
        } else {
          agreement.getLots().stream().forEach(lot -> {
            updatedJourneyInstance.addJourneyInstanceOutcomeDetails(
                createJourneyInstanceOutcomeDetails(agreement.getNumber(),
                    Optional.of(lot.getNumber())));
          });
        }
      });
    }
    journeyInstanceRepo.saveAndFlush(updatedJourneyInstance);
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

}
