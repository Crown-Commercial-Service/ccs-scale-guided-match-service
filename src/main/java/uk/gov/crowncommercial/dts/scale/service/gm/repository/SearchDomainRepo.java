package uk.gov.crowncommercial.dts.scale.service.gm.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.gov.crowncommercial.dts.scale.service.gm.model.entity.SearchDomain;

/**
 *
 */
@Repository
public interface SearchDomainRepo extends JpaRepository<SearchDomain, Integer> {

  Optional<SearchDomain> findByLookupEntryId(UUID lookupEntryId);

  @Query(
      value = "SELECT * FROM search_terms st INNER JOIN search_domains sd ON st.search_id = sd.search_id WHERE search_term % ?1",
      nativeQuery = true)
  List<SearchDomain> findBySearchTermFuzzyMatch(String searchTerm);

}
