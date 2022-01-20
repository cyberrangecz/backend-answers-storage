package cz.muni.ics.kypo.answers.storage;

import cz.muni.ics.kypo.answers.storage.config.PersistenceConfigTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(PersistenceConfigTest.class)
@SpringBootTest
class KypoAnswersStorageApplicationTests {

    @Test
    void contextLoads() {
    }

}
