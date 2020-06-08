package uk.gov.crowncommercial.dts.scale.service.gm.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Value;

/**
 *
 */
@Value
public class AnswerDefinition {

  @JsonAlias("uuid")
  String id;
  String text;
  String hint;
  int order;
  Boolean mutuallyExclusive;
  ConditionalInput conditionalInput;

}
