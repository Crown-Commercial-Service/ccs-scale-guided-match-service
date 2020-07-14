package uk.gov.crowncommercial.dts.scale.service.gm.model.external.dt;

import java.util.Set;
import lombok.Builder;
import lombok.Value;
import uk.gov.crowncommercial.dts.scale.service.gm.model.AnswerDefinition;
import uk.gov.crowncommercial.dts.scale.service.gm.model.Question;
import uk.gov.crowncommercial.dts.scale.service.gm.model.QuestionType;
import uk.gov.crowncommercial.dts.scale.service.gm.model.Unit;

/**
 * Decision Tree service question definition. Analogous to {@link Question}
 */
@Value
@Builder
public class DTQuestionDefinition {

  String uuid;
  String text;
  String hint;
  String pattern;
  QuestionType type;
  Set<AnswerDefinition> answerDefinitions;
  Unit unit;

}
