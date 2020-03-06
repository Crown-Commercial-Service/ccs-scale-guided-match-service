package uk.gov.crowncommercial.dts.scale.model;

import lombok.Data;
import lombok.NonNull;

/**
 * Journey.
 */
@Data
public class Journey {

	@NonNull
	private Long id;

	@NonNull
	private String name;

}
