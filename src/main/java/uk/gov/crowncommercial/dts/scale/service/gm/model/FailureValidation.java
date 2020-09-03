package uk.gov.crowncommercial.dts.scale.service.gm.model;

import lombok.Value;

/**
 * Failure validation details to inform UI of how to validate answer(s) to a question
 */
@Value
public class FailureValidation {

  String failureValidationTypeCode;
  String errorCode;
  String errorMessage;
  String errorSummary;

}
