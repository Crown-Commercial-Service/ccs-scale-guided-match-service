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
@Table(name = "journey_instance_outcome_details")
public class JourneyInstanceOutcomeDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "journey_instance_outcome_detail_id")
  Long id;

  @Column(name = "journey_instance_id")
  Long journeyInstanceId;

  @Column(name = "agreement_number")
  String agreementNumber;

  @Column(name = "lot_number")
  String lotNumber;

}
