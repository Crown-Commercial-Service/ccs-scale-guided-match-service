package uk.gov.crowncommercial.dts.scale.service.gm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 *
 */
@Entity
@Table(name = "search_terms")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchTerm {

  @Id
  @Column(name = "search_id")
  Integer id;

  @Column(name = "search_term")
  String term;

}
