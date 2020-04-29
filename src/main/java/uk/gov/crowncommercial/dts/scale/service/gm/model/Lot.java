package uk.gov.crowncommercial.dts.scale.service.gm.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

/**
 * Commercial agreement lot
 */
@JsonDeserialize(builder = Lot._Builder.class)
@Value
@Builder(builderClassName = "_Builder")
public class Lot {

  String number;

  /**
   * Primary RTM - {@link RouteToMarket#BAT} or {@link RouteToMarket#CAT}
   */
  RouteToMarket type;

  /**
   * Secondary RTM - {@link RouteToMarket#DA} or {@link RouteToMarket#FC}
   */
  RouteToMarket routeToMarket;
  String url;

  @JsonPOJOBuilder(withPrefix = "")
  public static class _Builder {
    // Enhanced by Lombok
  }
}
