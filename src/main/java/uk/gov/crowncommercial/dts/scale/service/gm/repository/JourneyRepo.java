package uk.gov.crowncommercial.dts.scale.service.gm.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.crowncommercial.dts.scale.service.gm.model.entity.Journey;

/**
 *
 */
@Repository
public interface JourneyRepo extends JpaRepository<Journey, UUID> {
  /* No additional repository methods */
}
