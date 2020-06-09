package uk.gov.crowncommercial.dts.scale.service.gm.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import uk.gov.crowncommercial.dts.scale.service.gm.model.AnsweredQuestion;
import uk.gov.crowncommercial.dts.scale.service.gm.model.GetJourneyHistoryResponse;
import uk.gov.crowncommercial.dts.scale.service.gm.model.QuestionDefinitionList;
import uk.gov.crowncommercial.dts.scale.service.gm.model.entity.Journey;
import uk.gov.crowncommercial.dts.scale.service.gm.model.entity.JourneyInstance;
import uk.gov.crowncommercial.dts.scale.service.gm.repository.JourneyInstanceRepo;

/**
 *
 */
@Service
@RequiredArgsConstructor
public class JourneyInstanceService {

  private final JourneyInstanceRepo journeyInstanceRepo;

  public GetJourneyHistoryResponse getJourneyHistory(final String journeyInstanceId) {

    // return dataLoader.convertJsonToObject("get-journey-history/" + journeyInstanceId + ".json",
    // GetJourneyHistoryResponse.class);
    return null;
  }

  public JourneyInstance createNewJourneyInstance(final Journey journey) {
    JourneyInstance journeyInstance = new JourneyInstance();
    journeyInstance.setUuid(UUID.randomUUID());
    journeyInstance.setJourney(journey);
    journeyInstance.setStartDate(LocalDate.now());
    return journeyInstanceRepo.saveAndFlush(journeyInstance);
  }

  public Optional<JourneyInstance> findByUuid(final UUID uuid) {
    return journeyInstanceRepo.findByUuid(uuid);
  }

  public void updateJourneyInstanceAnswers(final JourneyInstance journeyInstance,
      final Set<AnsweredQuestion> answeredQuestions) {
    // TODO Auto-generated method stub

  }

  public void updateJourneyInstanceAnswers(final JourneyInstance journeyInstance,
      final QuestionDefinitionList questionDefinitionList) {
    // TODO Auto-generated method stub

  }


}
