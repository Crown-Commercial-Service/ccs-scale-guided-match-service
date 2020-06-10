package uk.gov.crowncommercial.dts.scale.service.gm.model;

import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Value;

/**
 *
 */
@Value
public class GivenAnswer {

  @JsonAlias("id")
  @Nullable
  String uuid;

  @Nullable
  String value;

}
