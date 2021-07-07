package cz.muni.ics.kypo.answers.storage;

import cz.muni.ics.kypo.answers.storage.config.PersistenceConfigTest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import(PersistenceConfigTest.class)
@SpringBootTest
class KypoAnswersStorageApplicationTests {

    @Test
    void contextLoads() {
    }

}
