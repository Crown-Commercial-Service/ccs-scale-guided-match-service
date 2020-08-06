package uk.gov.crowncommercial.dts.scale.service.gm.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.gm.repository.SearchDomainRepo;

@SpringBootTest
@ActiveProfiles("test")
public class SearchTermLookupServiceTest {

  private static final String SEARCH_TERM = "linen";

  @MockBean
  private SearchDomainRepo mockSearchDomainRepo;


  @Test
  public void testGetAgreement() throws Exception {
    // when(mockSearchDomainRepo.findBySearchTermFuzzyMatch(SEARCH_TERM))
    // .thenReturn(mockCommercialAgreement);
    // assertEquals(mockCommercialAgreement, service.findAgreementByNumber(AGREEMENT_NUMBER));
  }

  // private SearchDomain
}
