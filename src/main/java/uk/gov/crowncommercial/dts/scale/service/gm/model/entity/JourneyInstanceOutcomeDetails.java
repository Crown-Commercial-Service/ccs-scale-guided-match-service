package uk.gov.crowncommercial.dts.scale.service.gm.model.entity;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 *
 */
@Entity
@Data
@EqualsAndHashCode(exclude = "journeyInstance")
@ToString(exclude = "journeyInstance")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "journey_instance_outcome_details")
public class JourneyInstanceOutcomeDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "journey_instance_outcome_detail_id")
  Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "journey_instance_id")
  JourneyInstance journeyInstance;

  @Column(name = "agreement_number")
  String agreementNumber;

  @Column(name = "lot_number")
  String lotNumber;

}
