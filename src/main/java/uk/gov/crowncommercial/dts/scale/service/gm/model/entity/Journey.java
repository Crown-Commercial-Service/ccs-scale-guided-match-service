package uk.gov.crowncommercial.dts.scale.service.gm.model.entity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "journeys")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Journey {

  @Id
  @Column(name = "journey_id")
  @Type(type = "pg-uuid")
  UUID id;

  @Column(name = "journey_name")
  String name;

  Boolean published;
  // String description;
  // UUID parentId;

}
