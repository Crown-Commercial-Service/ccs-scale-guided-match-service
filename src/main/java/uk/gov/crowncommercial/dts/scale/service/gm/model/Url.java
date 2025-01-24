package uk.gov.crowncommercial.dts.scale.service.gm.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Value;

/**
 * Url endpoint
 */
@Value
@JsonTypeName("url")
public class Url {

  private String text;
  private String hint;
  private String link;

}