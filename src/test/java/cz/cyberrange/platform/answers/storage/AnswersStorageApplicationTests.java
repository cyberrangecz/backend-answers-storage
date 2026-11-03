package cz.cyberrange.platform.answers.storage;

import cz.cyberrange.platform.answers.storage.config.PersistenceConfigTest;
import cz.cyberrange.platform.answers.storage.config.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import({PersistenceConfigTest.class, TestcontainersConfiguration.class})
@SpringBootTest
class AnswersStorageApplicationTests {

  @Test
  void contextLoads() {}
}
