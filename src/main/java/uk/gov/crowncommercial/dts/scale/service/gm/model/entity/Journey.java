package uk.gov.crowncommercial.dts.scale.service.gm.model.entity;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 *
 */
@Entity
@Table(name = "journeys")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Journey {

  UUID id;
  String name;
  Boolean published;
  // String description;
  // UUID parentId;

}
