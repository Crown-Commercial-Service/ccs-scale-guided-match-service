package uk.gov.crowncommercial.dts.scale.service.gm.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Route to market primary ({@link #BAT} or {@link #CAT} and sub-types ({@link #DA} or {@link #FC})
 */
public enum RouteToMarket {

  /**
   * Buy-a-Thing
   */
  BAT,

  /**
   * Contract-a-Thing
   */
  CAT,

  /**
   * Direct Award
   */
  DA,

  /**
   * Further Competition
   */
  FC;

  @JsonValue
  public String getName() {
    return this.name().toLowerCase();
  }

}
