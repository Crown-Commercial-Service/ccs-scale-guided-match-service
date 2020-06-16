package uk.gov.crowncommercial.dts.scale.service.gm.model.entity;

import java.time.LocalDate;
import java.util.UUID;
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
@EqualsAndHashCode(exclude = "journeyInstanceQuestion")
@ToString(exclude = "journeyInstanceQuestion")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "journey_instance_answers")
public class JourneyInstanceAnswer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "journey_instance_answer_id")
  Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "journey_instance_question_id")
  JourneyInstanceQuestion journeyInstanceQuestion;

  @Column(columnDefinition = "uuid", name = "journey_answer_id")
  UUID answerId;

  @Column(name = "answer_sequence")
  Short order;

  @Column(name = "answer_text")
  String answerText;

  @Column(name = "answer_date")
  LocalDate answerDate;

  @Column(name = "answer_number", columnDefinition = "NUMERIC", length = 20, precision = 4)
  Double answerNumber;
}
