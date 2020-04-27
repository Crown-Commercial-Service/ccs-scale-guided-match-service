package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.Set;
import lombok.Value;

/**
 * CCS commercial agreement (aka framework)
 */
@Value
public class Agreement {

  String number;
  boolean scale;

  /**
   * Should be {@link RouteToMarket#BAT} or {@link RouteToMarket#CAT} at agreement level.
   */
  RouteToMarket type;
  Set<Lot> lots;

}
