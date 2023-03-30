package uk.gov.crowncommercial.dts.scale.service.gm.model.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import uk.gov.crowncommercial.dts.scale.service.gm.model.OutcomeType;

/**
 *
 */
@Entity
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

  @Column(name = "selected_domain")
  String selectedDomain;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "journeyInstance")
  Set<JourneyInstanceQuestion> journeyInstanceQuestions = new HashSet<>();

  @Column(name = "start_datetime")
  LocalDateTime startDateTime;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "journeyInstance")
  Set<JourneyInstanceOutcomeDetails> journeyInstanceOutcomeDetails = new HashSet<>();

  @Column(name = "end_datetime")
  LocalDateTime endDateTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "outcome_type")
  OutcomeType outcomeType;

  public void addJourneyInstanceOutcomeDetails(final JourneyInstanceOutcomeDetails joid) {
    journeyInstanceOutcomeDetails.add(joid);
    joid.setJourneyInstance(this);
  }

  public void clearJourneyInstanceOutcomeDetails() {
    journeyInstanceOutcomeDetails.stream().forEach(jiod -> jiod.setJourneyInstance(null));
    journeyInstanceOutcomeDetails.clear();
  }

  public void addJourneyInstanceQuestion(final JourneyInstanceQuestion jiq) {
    journeyInstanceQuestions.add(jiq);
    jiq.setJourneyInstance(this);
  }

}
