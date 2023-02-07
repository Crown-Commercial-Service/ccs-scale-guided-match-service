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
   * @param searchTerm
   * @return List of SearchDomain objects that have been fuzzy matched on searchTerm
   */
  @Query(
      value = "SELECT DISTINCT sd.journey_id\\:\\:varchar, modifier_journey_name, journey_selection_text, journey_selection_description "
          + "FROM search_terms st INNER JOIN search_domains sd ON st.search_id = sd.search_id "
    	  + "JOIN journeys j2 ON j2.journey_id = sd.journey_id "
          + "WHERE SIMILARITY(search_term,?1) > 0.63 "
    	  + "AND j2.published = true ",
      nativeQuery = true)
  List<Object[]> findBySearchTermFuzzyMatch(String searchTerm);

}
