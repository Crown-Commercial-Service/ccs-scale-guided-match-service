package uk.gov.crowncommercial.dts.scale.service.gm.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import uk.gov.crowncommercial.dts.scale.service.gm.model.*;
import uk.gov.crowncommercial.dts.scale.service.gm.model.entity.*;
import uk.gov.crowncommercial.dts.scale.service.gm.repository.JourneyInstanceRepo;
import uk.gov.crowncommercial.dts.scale.service.gm.temp.DataLoader;

/**
 *
 */
@Service
@RequiredArgsConstructor
public class JourneyInstanceService {

  private final JourneyInstanceRepo journeyInstanceRepo;
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
        jia.setAnswerDate(LocalDate.now());
        jia.setAnswerId(UUID.fromString(a.getUuid()));
        jia.setAnswerText(a.getValue());
        journeyInstanceQuestion.getJourneyInstanceAnswers().add(jia);
      });

    });
    journeyInstanceRepo.saveAndFlush(journeyInstance);
  }

  public void updateJourneyInstanceQuestions(final JourneyInstance journeyInstance,
      final QuestionDefinitionList questionDefinitionList) {

    questionDefinitionList.stream().map(QuestionDefinition::getQuestion).forEach(q -> {
      JourneyInstanceQuestion jiq = new JourneyInstanceQuestion();
      jiq.setUuid(UUID.fromString(q.getId()));
      jiq.setJourneyInstanceId(journeyInstance.getId());
      jiq.setText(q.getText());
      jiq.setHint(q.getHint());
      jiq.setType(q.getType());
      jiq.setOrder((short) 1);
      journeyInstance.getJourneyInstanceQuestions().add(jiq);
    });

    journeyInstanceRepo.saveAndFlush(journeyInstance);
  }

  public void updateJourneyInstanceOutcome(final JourneyInstance journeyInstance,
      final OutcomeType outcomeType, final Optional<OutcomeData> outcomeData) {
    journeyInstance.setEndDateTime(LocalDateTime.now(clock));
    journeyInstance.setOutcomeType(outcomeType);

    if (outcomeType == OutcomeType.AGREEMENT) {
      AgreementList agreementList = (AgreementList) outcomeData
          .orElseThrow(() -> new RuntimeException("TODO: 500 Agreement outcome without agreement"));

      Set<JourneyInstanceOutcomeDetails> jiods = journeyInstance.getJourneyInstanceOutcomeDetails();

      agreementList.stream().forEach(agreement -> {

        if (agreement.getLots().isEmpty()) {
          jiods.add(createJourneyInstanceOutcomeDetails(journeyInstance.getId(),
              agreement.getNumber(), Optional.empty()));
        } else {
          agreement.getLots().stream().forEach(lot -> {
            jiods.add(createJourneyInstanceOutcomeDetails(journeyInstance.getId(),
                agreement.getNumber(), Optional.of(lot.getNumber())));
          });
        }
      });
    }
    journeyInstanceRepo.saveAndFlush(journeyInstance);
  }

  private JourneyInstanceOutcomeDetails createJourneyInstanceOutcomeDetails(
      final Long journeyInstanceId, final String agreementNumber,
      final Optional<String> lotNumber) {
    JourneyInstanceOutcomeDetails jiod = new JourneyInstanceOutcomeDetails();
    jiod.setJourneyInstanceId(journeyInstanceId);
    jiod.setAgreementNumber(agreementNumber);
    if (lotNumber.isPresent()) {
      jiod.setLotNumber(lotNumber.get());
    }
    return jiod;
  }

}
