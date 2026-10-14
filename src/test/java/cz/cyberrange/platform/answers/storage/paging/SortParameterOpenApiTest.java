package cz.cyberrange.platform.answers.storage.paging;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cz.cyberrange.platform.answers.storage.config.PersistenceConfigTest;
import cz.cyberrange.platform.answers.storage.config.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

/** Checks how the generated OpenAPI document describes the sort parameter of the paged endpoint. */
@Import({PersistenceConfigTest.class, TestcontainersConfiguration.class})
@SpringBootTest
@AutoConfigureMockMvc
class SortParameterOpenApiTest {

  private static final String SORT_PARAMETER =
      "$.paths./sandboxes.get.parameters[?(@.name=='sort')]";

  @Value("${server.servlet.context-path}")
  private String contextPath;

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("apiDocs_sortParameter_documentsEnumDefaultOrderAndIdTiebreaker")
  void apiDocs_sortParameter_documentsEnumDefaultOrderAndIdTiebreaker() throws Exception {
    mockMvc
        .perform(get(contextPath + "/v3/api-docs").contextPath(contextPath))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(SORT_PARAMETER + ".schema.items.enum[*]")
                .value(
                    containsInAnyOrder(
                        "id,asc",
                        "id,desc",
                        "sandboxRefId,asc",
                        "sandboxRefId,desc",
                        "allocationId,asc",
                        "allocationId,desc",
                        "accessToken,asc",
                        "accessToken,desc",
                        "userId,asc",
                        "userId,desc")))
        .andExpect(jsonPath(SORT_PARAMETER + ".schema.default[0]").value("id,asc"))
        .andExpect(
            jsonPath(SORT_PARAMETER + ".description")
                .value(hasItem(containsString("ends with id,asc"))));
  }
}
