package cz.muni.ics.kypo.answers.storage.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.boot.starter.autoconfigure.OpenApiAutoConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ConditionalOnExpression(value = "${swagger.enabled:false}")
public class SwaggerConfig extends OpenApiAutoConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("public-api")
                .apiInfo(apiInfo()).useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("cz.muni.ics.kypo.answers.storage.rest"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("KYPO Answers Storage - API Reference")
                .description("Developed by KYPO team")
                .license("MIT License")
                .licenseUrl("https://opensource.org/licenses/MIT")
                .build();
    }

}