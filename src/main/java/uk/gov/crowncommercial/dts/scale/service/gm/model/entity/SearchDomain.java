package uk.gov.crowncommercial.dts.scale.service.gm.model.entity;

import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;
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

  @Column(name = "journey_id")
  @Type(type = "pg-uuid")
  UUID journeyId;

  @Column(name = "modifier_journey_name")
  String modifierJourneyName;

  @Column(name = "modifier_journey_description")
  String modifierJourneyDescription;

}
