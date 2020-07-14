package uk.gov.crowncommercial.dts.scale.service.gm.model;

import lombok.Value;

/**
 *
 */
@Value
public class Question {

  String id;
  String text;
  String hint;
  QuestionType type;
  Unit unit;

}
