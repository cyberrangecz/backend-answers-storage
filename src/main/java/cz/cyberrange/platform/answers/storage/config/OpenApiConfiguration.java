package cz.cyberrange.platform.answers.storage.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.util.Optional;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declares the OpenAPI document metadata and the schema model resolver used to generate it.
 */
@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openApi(Optional<BuildProperties> buildProperties) {
        Info info = new Info().title("CyberRangeCZ Platform Answers Storage - API Reference");
        buildProperties.ifPresent(properties -> info.setVersion(properties.getVersion()));
        return new OpenAPI().info(info);
    }

    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }
}
