package uk.gov.crowncommercial.dts.scale.service.gm.model.entity;

import java.time.LocalDate;
import java.util.Set;
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
@Table(name = "journey_instances")
public class JourneyInstance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "journey_instance_id")
  Long id;

  @Column(name = "journey_instance_uuid")
  @Type(type = "pg-uuid")
  UUID uuid;

  @ManyToOne
  @JoinColumn(name = "journey_id")
  Journey journey;

  @OneToMany
  @JoinColumn(name = "journey_instance_id")
  Set<JourneyInstanceQuestion> journeyInstanceQuestions;

  @Column(name = "journey_start_date")
  LocalDate startDate;

  @Column(name = "journey_end_date")
  LocalDate endDate;

}