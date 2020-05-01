package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.ArrayList;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 *
 */
@JsonTypeName("question")
public class QuestionDefinitionList extends ArrayList<QuestionDefinition> implements OutcomeData {

  /**
   * Default <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 1L;

  @JsonCreator(mode = Mode.DELEGATING)
  public QuestionDefinitionList(final Collection<QuestionDefinition> lots) {
    super(lots);
  }
}
