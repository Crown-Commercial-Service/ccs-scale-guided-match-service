package uk.gov.crowncommercial.dts.scale.service.gm.temp;

import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Throw-away component for interim Mock Service to load sample data from JSON files (will be
 * deleted in actual implementation and replaced by Database).
 *
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader {

  private static final String MOCK_DATA_PATH = "/mock-data/";

  private final ObjectMapper objectMapper;
  private final ResourceLoader resourceLoader;

  public <T> T convertJsonToObject(final String filename, final Class<T> target) {
    try {
      Resource resource = resourceLoader.getResource("classpath:" + MOCK_DATA_PATH + filename);
      return objectMapper.readValue(resource.getInputStream(), target);
    } catch (Exception e) {
      log.error(e.toString(), e);
      // RuntimeException is just a convenience for throwaway class
      throw new RuntimeException("Unable to load mock data from file: " + filename);
    }
  }

  public <T> T convertJsonToList(final String filename, final Class<T> target) {

    try {
      Resource resource = resourceLoader.getResource("classpath:" + MOCK_DATA_PATH + filename);
      return objectMapper.readValue(resource.getInputStream(),
          objectMapper.getTypeFactory().constructCollectionType(List.class, target));

    } catch (Exception e) {
      log.error(e.toString(), e);
      // RuntimeException is just a convenience for throwaway class
      throw new RuntimeException("Unable to load mock data from file: " + filename);
    }
  }
}
