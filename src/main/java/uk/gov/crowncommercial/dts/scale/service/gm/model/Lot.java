package uk.gov.crowncommercial.dts.scale.service.gm.model;

import lombok.Value;

/**
 * Commercial agreement lot
 */
@Value
public class Lot {

  String number;

  /**
   * Should be {@link RouteToMarket#DA} or {@link RouteToMarket#FC} at lot level.
   */
  RouteToMarket routeToMarket;
  String url;
}
