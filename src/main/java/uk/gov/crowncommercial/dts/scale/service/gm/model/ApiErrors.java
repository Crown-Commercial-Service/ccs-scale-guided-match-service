package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.List;

import lombok.Data;

/**
 * Collection of {@link ApiError} objects.
 */
@Data
public class ApiErrors {

	private final List<ApiError> errors;
}
