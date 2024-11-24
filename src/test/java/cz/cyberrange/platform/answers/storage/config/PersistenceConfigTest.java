package cz.cyberrange.platform.answers.storage.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "cz.cyberrange.platform.answers.storage.data.entities")
@EnableJpaRepositories(basePackages = "cz.cyberrange.platform.answers.storage.data.repositories")
public class PersistenceConfigTest {

}
