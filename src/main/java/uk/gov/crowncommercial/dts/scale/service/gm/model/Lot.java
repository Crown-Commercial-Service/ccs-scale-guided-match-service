package uk.gov.crowncommercial.dts.scale.service.gm.model;

import lombok.Builder;
import lombok.Value;

/**
 * Commercial agreement lot
 */
@Value
@Builder
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
  boolean scale;

}
