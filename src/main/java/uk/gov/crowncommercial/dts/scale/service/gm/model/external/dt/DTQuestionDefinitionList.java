package uk.gov.crowncommercial.dts.scale.service.gm.model.external.dt;

import java.util.ArrayList;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonTypeName;
import uk.gov.crowncommercial.dts.scale.service.gm.model.Agreement;
import uk.gov.crowncommercial.dts.scale.service.gm.model.OutcomeData;

/**
 * Wrapper type for lists of {@link Agreement}s
 */
@JsonTypeName("agreement")
public class DTQuestionDefinitionList extends ArrayList<DTQuestionDefinition>
    implements OutcomeData {

  /**
   * Default <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 1L;

  @JsonCreator(mode = Mode.DELEGATING)
  public DTQuestionDefinitionList(final Collection<DTQuestionDefinition> questionDefinitions) {
    super(questionDefinitions);
  }

}
