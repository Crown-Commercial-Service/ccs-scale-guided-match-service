package uk.gov.crowncommercial.dts.scale.service.gm.model.entity;

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
@Table(name = "journey_instance_questions")
public class JourneyInstanceQuestion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "journey_instance_question_id")
  Long id;

  @OneToMany
  @JoinColumn(name = "journey_instance_question_id")
  Set<JourneyInstanceAnswer> journeyInstanceAnswer;

  @Column(name = "journey_question_id")
  @Type(type = "pg-uuid")
  UUID questionId;

  @Column(name = "question_order")
  Short order;

  @Column(name = "question_text")
  String text;

}
