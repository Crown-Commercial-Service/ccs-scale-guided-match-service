package uk.gov.crowncommercial.dts.scale.service.gm.service;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.gm.model.FailureValidation;
import uk.gov.crowncommercial.dts.scale.service.gm.model.entity.ErrorUsage;
import uk.gov.crowncommercial.dts.scale.service.gm.repository.ErrorUsageRepo;

/**
 * Interrogates the error usage persistence layer for details of failure validation and error codes
 * mapped to question definitions, for use by the UI
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FailureValidationService {

  private final ErrorUsageRepo errorUsageRepo;

  public Set<FailureValidation> findFailureValidationByQuestionDefId(final String questionDefId) {

    UUID questionDefUuid = UUID.fromString(questionDefId);

    Set<ErrorUsage> errorUsages = errorUsageRepo.findErrorUsageByQuestionId(questionDefUuid);

    if (CollectionUtils.isEmpty(errorUsages)) {
      log.warn("Found zero error usage entries for Question Definition UUID: {}", questionDefUuid);
      return Collections.emptySet();
    }

    return errorUsages.stream()
        .map(eu -> new FailureValidation(
            eu.getFailureValidationType().getFailureValidationTypeCode(),
            eu.getErrorMessage().getErrorMessageCode(), eu.getErrorMessage().getErrorMessage(),
            eu.getErrorMessage().getErrorSummary()))
        .collect(Collectors.toSet());
  }
}
