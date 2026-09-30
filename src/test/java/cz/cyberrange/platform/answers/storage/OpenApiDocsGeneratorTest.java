package cz.cyberrange.platform.answers.storage;

import cz.cyberrange.platform.answers.storage.config.PersistenceConfigTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Writes the springdoc OpenAPI document to the directory given by the {@code docs.output.directory}
 * system property, which the {@code docs} Maven profile sets. Skipped in regular test runs.
 */
@Import(PersistenceConfigTest.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnabledIfSystemProperty(named = "docs.output.directory", matches = ".+")
class OpenApiDocsGeneratorTest {

    private static final String CONTEXT_PATH = "/answers-storage/api/v1";
    private static final String FILE_NAME = "answers-storage-swagger-docs.yaml";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void generateOpenApiDocs() throws Exception {
        String yaml = mockMvc.perform(get(CONTEXT_PATH + "/v3/api-docs.yaml").contextPath(CONTEXT_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        Path directory = Paths.get(System.getProperty("docs.output.directory"));
        Files.createDirectories(directory);
        Files.writeString(directory.resolve(FILE_NAME), yaml, StandardCharsets.UTF_8);
    }
}
