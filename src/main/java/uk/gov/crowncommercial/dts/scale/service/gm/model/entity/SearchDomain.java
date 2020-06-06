package uk.gov.crowncommercial.dts.scale.service.gm.model.entity;

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

  @Column(name = "modifier_journey_name")
  String modifierJourneyName;

  @Column(name = "modifier_journey_description")
  String modifierJourneyDescription;

}
