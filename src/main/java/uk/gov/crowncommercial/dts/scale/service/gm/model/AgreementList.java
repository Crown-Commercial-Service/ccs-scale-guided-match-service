package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.ArrayList;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Wrapper type for lists of {@link Agreement}s
 */
@JsonTypeName("agreement")
public class AgreementList extends ArrayList<Agreement> implements OutcomeData {

  /**
   * Default <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 1L;

  @JsonCreator(mode = Mode.DELEGATING)
  public AgreementList(final Collection<Agreement> agreements) {
    super(agreements);
  }

}
