package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Value;

/**
 *
 */
@Value
public class AnsweredQuestion {

  @JsonAlias("id")
  String uuid;
  Set<GivenAnswer> answers;

}
