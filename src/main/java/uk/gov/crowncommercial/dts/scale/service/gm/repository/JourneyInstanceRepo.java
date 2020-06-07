package uk.gov.crowncommercial.dts.scale.service.gm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.crowncommercial.dts.scale.service.gm.model.entity.JourneyInstance;

/**
 *
 */
@Repository
public interface JourneyInstanceRepo extends JpaRepository<JourneyInstance, Long> {

}
