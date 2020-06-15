package uk.gov.crowncommercial.dts.scale.service.gm.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.gov.crowncommercial.dts.scale.service.gm.model.entity.JourneyInstance;
import uk.gov.crowncommercial.dts.scale.service.gm.model.entity.JourneyInstanceQuestion;

/**
 *
 */
@Repository
public interface JourneyInstanceQuestionRepo extends JpaRepository<JourneyInstanceQuestion, Long> {

  Optional<JourneyInstanceQuestion> findByJourneyInstanceAndUuid(JourneyInstance journeyInstance,
      UUID uuid);

  @Modifying
  @Query("delete from JourneyInstanceQuestion jiq where jiq.journeyInstance = :journeyInstance and jiq.order >= :order")
  void deleteByJourneyInstanceAndOrder(@Param("journeyInstance") JourneyInstance journeyInstance,
      @Param("order") Short order);

  Optional<JourneyInstanceQuestion> findTopByJourneyInstanceOrderByOrderDesc(
      JourneyInstance journeyInstance);
}
