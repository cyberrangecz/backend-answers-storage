package cz.cyberrange.platform.answers.storage.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import cz.cyberrange.platform.answers.storage.service.SandboxAnswersService;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import java.util.Optional;
import java.util.stream.Stream;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.customizers.QuerydslPredicateOperationCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.web.method.HandlerMethod;

/**
 * Declares the OpenAPI document metadata, the schema model resolver used to generate it, and the
 * document generator setting for QueryDSL predicates.
 */
@Configuration
public class OpenApiConfiguration {

  @Bean
  public OpenAPI openApi(Optional<BuildProperties> buildProperties) {
    Info info = new Info().title("CyberRangeCZ Platform Answers Storage - API Reference");
    buildProperties.ifPresent(properties -> info.setVersion(properties.getVersion()));
    return new OpenAPI().info(info);
  }

  /**
   * Resolves schema property names in snake_case, matching the names written in JSON bodies.
   *
   * @return the schema model resolver
   */
  @Bean
  public ModelResolver modelResolver() {
    ObjectMapper schemaObjectMapper = new ObjectMapper();
    schemaObjectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    return new ModelResolver(schemaObjectMapper);
  }

  /**
   * Builds the document generator's QueryDSL operation customizer as one that leaves operations
   * unchanged, so a QueryDSL predicate argument is documented as the single {@code predicate} query
   * parameter rather than as one query parameter per field of the predicate's root entity.
   *
   * @param querydslBindingsFactory the factory of the QueryDSL bindings predicates are bound with
   * @param springDocConfigProperties the document generator's settings
   * @return the customizer registered in place of the generator's default one
   */
  @Bean
  public QuerydslPredicateOperationCustomizer querydslPredicateOperationCustomizer(
      QuerydslBindingsFactory querydslBindingsFactory,
      SpringDocConfigProperties springDocConfigProperties) {
    return new QuerydslPredicateOperationCustomizer(
        querydslBindingsFactory, springDocConfigProperties) {
      @Override
      public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        return operation;
      }
    };
  }

  /**
   * Documents the {@code sort} parameter of every operation: the accepted {@code
   * property,(asc|desc)} values as an enum, the default order, and the id order that every sort
   * ends with.
   *
   * @return the customizer applied to each generated operation
   */
  @Bean
  public OperationCustomizer sortParameterOperationCustomizer() {
    return (operation, handlerMethod) -> {
      if (operation.getParameters() != null) {
        operation.getParameters().stream()
            .filter(parameter -> "sort".equals(parameter.getName()))
            .forEach(OpenApiConfiguration::documentSortParameter);
      }
      return operation;
    };
  }

  private static void documentSortParameter(Parameter sortParameter) {
    sortParameter.setDescription(
        ("Sorting criteria as property,(asc|desc); the direction defaults to asc and the parameter "
                + "may repeat. Sortable properties: %s. Without a sort the order is %s,asc. Every "
                + "sort ends with %s,asc, so pages never overlap. Any other property answers HTTP 400.")
            .formatted(
                String.join(", ", SandboxAnswersService.SORTABLE_PROPERTIES),
                SandboxAnswersService.TIEBREAKER_PROPERTY,
                SandboxAnswersService.TIEBREAKER_PROPERTY));
    sortParameter
        .getSchema()
        .setDefault(
            JsonNodeFactory.instance
                .arrayNode()
                .add(SandboxAnswersService.TIEBREAKER_PROPERTY + ",asc"));
    sortParameter
        .getSchema()
        .getItems()
        .setEnum(
            SandboxAnswersService.SORTABLE_PROPERTIES.stream()
                .flatMap(property -> Stream.of(property + ",asc", property + ",desc"))
                .toList());
  }
}
