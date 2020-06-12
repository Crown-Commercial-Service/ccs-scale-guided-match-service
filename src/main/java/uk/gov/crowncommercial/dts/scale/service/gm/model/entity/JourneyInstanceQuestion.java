package uk.gov.crowncommercial.dts.scale.service.gm.model.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import uk.gov.crowncommercial.dts.scale.service.gm.model.QuestionType;

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

  @Column(name = "journey_instance_id")
  Long journeyInstanceId;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "journey_instance_question_id")
  Set<JourneyInstanceAnswer> journeyInstanceAnswers = new HashSet<>();

  @Column(columnDefinition = "uuid", name = "journey_question_id")
  UUID uuid;

  @Column(name = "question_order")
  Short order;

  @Column(name = "question_text")
  String text;

  @Column(name = "question_hint")
  String hint;

  @Enumerated(EnumType.STRING)
  @Column(name = "question_type")
  QuestionType type;

  // public Set<JourneyInstanceAnswer> getJourneyInstanceAnswers() {
  // if (journeyInstanceAnswers == null) {
  // journeyInstanceAnswers = new HashSet<>();
  // }
  // return journeyInstanceAnswers;
  // }

}
