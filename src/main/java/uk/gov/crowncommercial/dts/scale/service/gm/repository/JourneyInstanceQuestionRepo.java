package uk.gov.crowncommercial.dts.scale.service.gm.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.crowncommercial.dts.scale.service.gm.model.entity.JourneyInstanceQuestion;

/**
 *
 */
@Repository
public interface JourneyInstanceQuestionRepo extends JpaRepository<JourneyInstanceQuestion, Long> {

  Optional<JourneyInstanceQuestion> findByUuid(UUID uuid);

}
