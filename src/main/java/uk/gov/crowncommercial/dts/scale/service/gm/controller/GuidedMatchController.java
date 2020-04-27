package uk.gov.crowncommercial.dts.scale.service.gm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.crowncommercial.dts.scale.service.gm.model.Question;
import uk.gov.crowncommercial.dts.scale.service.gm.model.QuestionType;

/**
 * Guided Match Controller.
 *
 */
@RestController
public class GuidedMatchController {

  Logger logger = LoggerFactory.getLogger(GuidedMatchController.class);

  @GetMapping("/journeys/{id}")
  public Question getJourney(@PathVariable(value = "id") final String id) {
    logger.debug("getJourney: {}", id);
    return new Question("123-abc-456-def", "Yes or No?", "Hint..", QuestionType.BOOLEAN);
  }

}
