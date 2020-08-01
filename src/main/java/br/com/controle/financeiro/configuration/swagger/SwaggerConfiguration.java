package br.com.controle.financeiro.configuration.swagger;

import java.util.Arrays;
import java.util.Collections;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors
                                                                             .basePackage("br.com.controle.financeiro"))
                                                      .paths(PathSelectors.ant("/**")).build()
                                                      .globalOperationParameters(Collections.singletonList(
                                                              new ParameterBuilder().name("X-Authorization-Firebase")
                                                                                    .description("Firebase Auth Token")
                                                                                    .modelRef(new ModelRef("string"))
                                                                                    .parameterType("header")
                                                                                    .required(false).build()));
    }

}
