package uk.gov.crowncommercial.dts.scale.service.gm.repository;

import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.crowncommercial.dts.scale.service.gm.model.entity.ErrorUsage;

/**
 *
 */
@Repository
public interface ErrorUsageRepo extends JpaRepository<ErrorUsage, Integer> {

  Set<ErrorUsage> findErrorUsageByQuestionId(UUID questionId);

}
