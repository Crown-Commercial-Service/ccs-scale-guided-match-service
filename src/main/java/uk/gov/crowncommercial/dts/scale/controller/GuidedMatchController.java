package uk.gov.crowncommercial.dts.scale.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import uk.gov.crowncommercial.dts.scale.model.Journey;

/**
 * GuidedMatchController.
 * 
 */
@RestController
public class GuidedMatchController {

	Logger logger = LoggerFactory.getLogger(GuidedMatchController.class);

	@GetMapping("/journeys/{id}")
	public Journey getJourney(@PathVariable(value = "id") Long id) {
		logger.debug("getJourney: {}", id);
		return new Journey(id, "Tech Products 2");
	}

}
