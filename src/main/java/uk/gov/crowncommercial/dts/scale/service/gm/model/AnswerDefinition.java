package uk.gov.crowncommercial.dts.scale.service.gm.model;

import lombok.Value;

/**
 *
 */
@Value
public class AnswerDefinition {

  String id;
  String text;
  String hint;
  int order;
  Boolean mutuallyExclusive;
  ConditionalInput conditionalInput;

}
