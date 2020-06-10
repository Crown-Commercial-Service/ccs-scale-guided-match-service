package uk.gov.crowncommercial.dts.scale.service.gm.model.entity;

import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 *
 */
@Entity
@Immutable
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "search_domains")
public class SearchDomain {

  @Id
  @Column(name = "domain_modifier_id")
  Integer domainModiferId;

  @ManyToOne
  @JoinColumn(name = "search_id")
  SearchTerm searchTerm;

  @ManyToOne
  @JoinColumn(name = "journey_id")
  Journey journey;

  @Column(columnDefinition = "uuid", name = "lookup_entry_id")
  UUID lookupEntryId;

  @Column(name = "modifier_journey_name")
  String modifierJourneyName;

  @Column(name = "journey_selection_text")
  String journeySelectionText;

  @Column(name = "journey_selection_description")
  String journeySelectionDescription;

}
