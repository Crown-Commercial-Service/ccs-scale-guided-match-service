package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

/**
 * CCS commercial agreement (aka framework)
 */
@JsonDeserialize(builder = Agreement._Builder.class)
@Value
@Builder(builderClassName = "_Builder")
@JsonTypeName("agreement")
public class Agreement {

  String number;
  boolean scale;

  /**
   * Should be {@link RouteToMarket#BAT} or {@link RouteToMarket#CAT} at agreement level.
   */
  RouteToMarket type;
  Set<Lot> lots;

  @JsonPOJOBuilder(withPrefix = "")
  public static class _Builder {
    // Enhanced by Lombok
  }

}
