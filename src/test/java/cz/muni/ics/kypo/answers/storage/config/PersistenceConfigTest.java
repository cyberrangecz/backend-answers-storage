package cz.muni.ics.kypo.answers.storage.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "cz.muni.ics.kypo.answers.storage.data.entities")
@EnableJpaRepositories(basePackages = "cz.muni.ics.kypo.answers.storage.data.repositories")
public class PersistenceConfigTest {

}
