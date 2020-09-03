package uk.gov.crowncommercial.dts.scale.service.gm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 *
 */
@Entity
@Immutable
@Table(name = "error_messages")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorMessages {

  @Id
  @Column(name = "error_message_code")
  String errorMessageCode;

  @Column(name = "error_summary")
  String errorSummary;

  @Column(name = "error_message")
  String errorMessage;

}
