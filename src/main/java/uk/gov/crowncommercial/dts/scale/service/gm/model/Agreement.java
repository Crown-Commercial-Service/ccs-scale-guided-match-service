package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Value;

/**
 * CCS commercial agreement (aka framework)
 */
@Value
@JsonTypeName("agreement")
public class Agreement {

  String number;
  Set<Lot> lots;

}
