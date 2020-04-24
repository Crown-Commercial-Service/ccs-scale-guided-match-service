package uk.gov.crowncommercial.dts.scale.service.gm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 *
 */
public class QuestionDefinitionList extends ArrayList<QuestionDefinition> implements OutcomeData {
  /**
  *
  */
  private static final long serialVersionUID = 1L;

  public QuestionDefinitionList(final Collection<QuestionDefinition> lots) {
    super(lots);
  }

  /**
   * Returns a new populated instance from a collection of Lot super types, e.g
   * {@link QuestionDefinition}
   *
   * @param items
   * @return a new instance containing the items
   */
  public static QuestionDefinitionList fromItems(
      final Collection<? super QuestionDefinition> items) {
    return new QuestionDefinitionList(
        items.stream().map(i -> (QuestionDefinition) i).collect(Collectors.toSet()));
  }
}
