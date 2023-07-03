package br.com.lcn.goldenRaspberryAwards.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final ResponseMessage m201 = simpleMessage(201, "Recurso criado");
    private final ResponseMessage m200put = simpleMessage(200, "Atualização ok");
    private final ResponseMessage m204del = simpleMessage(204, "Deleção ok");
    private final ResponseMessage m404 = simpleMessage(404, "Não encontrado");
    private final ResponseMessage m500 = simpleMessage(500, "Erro inesperado");

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, Arrays.asList(m404, m500))
                .globalResponseMessage(RequestMethod.POST, Arrays.asList(m201, m500))
                .globalResponseMessage(RequestMethod.PUT, Arrays.asList(m200put, m404, m500))
                .globalResponseMessage(RequestMethod.PATCH, Arrays.asList(m200put, m404, m500))
                .globalResponseMessage(RequestMethod.DELETE, Arrays.asList(m204del, m404, m500))
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.lcn.goldenRaspberryAwards.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "API para avaliação da Texo IT",
                "Esta API é utilizada para avaliação da empresa Texo IT",
                "Versão 1.0",
                "",
                new Contact("Lucas Neves", "https://www.linkedin.com/in/lucas-neves-b822801b4/", "lucneves@gmail.com"),
                "",
                "",
                Collections.emptyList() // Vendor Extensions
        );
    }


    private ResponseMessage simpleMessage(int code, String msg) {
        return new ResponseMessageBuilder().code(code).message(msg).build();
    }
}