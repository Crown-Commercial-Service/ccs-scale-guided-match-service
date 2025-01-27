package uk.gov.crowncommercial.dts.scale.service.gm.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Wrapper type for lists of {@link Url}s
 */
@JsonTypeName("url")
public class UrlList extends ArrayList<Url> implements OutcomeData {

  /**
   * Default <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 1L;

  @JsonCreator(mode = Mode.DELEGATING)
  public UrlList(final Collection<Url> url) {
    super(url);
  }

}
