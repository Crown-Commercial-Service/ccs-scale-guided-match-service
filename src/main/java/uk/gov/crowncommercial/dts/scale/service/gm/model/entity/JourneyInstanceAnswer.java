package uk.gov.crowncommercial.dts.scale.service.gm.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import uk.gov.crowncommercial.dts.scale.service.gm.model.Unit;

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

  @Column(name = "value_number")
  BigDecimal valueNumber;

  @Column(name = "value_text")
  String valueText;

  @Column(name = "value_date")
  LocalDate valueDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "unit")
  Unit unit;
}
