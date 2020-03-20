package uk.gov.crowncommercial.dts.scale.gm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import uk.gov.crowncommercial.dts.scale.gm.model.Journey;

/**
 * Guided Match Controller.
 * 
 */
@RestController
public class GuidedMatchController {

	Logger logger = LoggerFactory.getLogger(GuidedMatchController.class);

	@GetMapping("/journeys/{id}")
	public Journey getJourney(@PathVariable(value = "id") String id) {
		logger.debug("getJourney: {}", id);
		return new Journey(id, "Journey " + id);
	}

}
