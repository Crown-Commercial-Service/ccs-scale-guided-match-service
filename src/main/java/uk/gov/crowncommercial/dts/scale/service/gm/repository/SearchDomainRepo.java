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

  /**
   * This native query requires `create extension pg_trgm;` running on database.
   * 
   * You can use a search similar to `SELECT * FROM search_terms WHERE
   * SIMILARITY(search_term,'linen') > 0.1;` for finer grain control over matches.
   * 
   * @param searchTerm
   * @return List of SearchDomain objects that have been fuzzy matched on searchTerm
   */
  @Query(
      value = "SELECT DISTINCT journey_id\\:\\:varchar, modifier_journey_name, journey_selection_text, journey_selection_description FROM search_terms st INNER JOIN search_domains sd ON st.search_id = sd.search_id WHERE search_term % ?1",
      nativeQuery = true)
  List<Object[]> findBySearchTermFuzzyMatch(String searchTerm);

}
