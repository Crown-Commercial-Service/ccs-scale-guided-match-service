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
@Table(name = "failure_validation_types")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FailureValidationTypes {

  @Id
  @Column(name = "failure_validation_type_code")
  String failureValidationTypeCode;

  @Column(name = "failure_validation_type_name")
  String failureValidationTypeName;

}
