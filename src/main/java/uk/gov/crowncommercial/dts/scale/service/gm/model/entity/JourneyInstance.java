package uk.gov.crowncommercial.dts.scale.service.gm.model.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
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
@Table(name = "journey_instances")
public class JourneyInstance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "journey_instance_id")
  Long id;

  @Column(columnDefinition = "uuid", name = "journey_instance_uuid")
  UUID uuid;

  @ManyToOne
  @JoinColumn(name = "journey_id")
  Journey journey;

  @Column(name = "original_search_term")
  String originalSearchTerm;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "journey_instance_id")
  Set<JourneyInstanceQuestion> journeyInstanceQuestions;

  @Column(name = "journey_start_date")
  LocalDate startDate;

  @Column(name = "journey_end_date")
  LocalDate endDate;

  public Set<JourneyInstanceQuestion> getJourneyInstanceQuestions() {
    if (journeyInstanceQuestions == null) {
      journeyInstanceQuestions = new HashSet<>();
    }
    return journeyInstanceQuestions;
  }

}
