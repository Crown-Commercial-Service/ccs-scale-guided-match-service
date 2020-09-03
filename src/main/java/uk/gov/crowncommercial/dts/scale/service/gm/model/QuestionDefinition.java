package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.Set;
import lombok.Value;

/**
 *
 */
@Value
public class QuestionDefinition {

  Question question;
  String pattern;
  String prePopulatedAnswerId;
  String prePopulatedAnswerSource;
  Set<AnswerDefinition> answerDefinitions;
  Set<FailureValidation> failureValidations;

}
