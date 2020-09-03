package uk.gov.crowncommercial.dts.scale.service.gm.model.entity;

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
@Table(name = "error_usage")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorUsage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "error_usage_id")
  Integer id;

  @ManyToOne
  @JoinColumn(name = "failure_validation_type_code")
  FailureValidationTypes failureValidationType;

  @ManyToOne
  @JoinColumn(name = "error_message_code")
  ErrorMessages errorMessage;

  @Column(columnDefinition = "uuid", name = "question_id")
  UUID questionId;

}
