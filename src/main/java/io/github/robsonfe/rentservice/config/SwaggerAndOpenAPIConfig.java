package io.github.robsonfe.rentservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerAndOpenAPIConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(
                        new Info()
                                .title("RentService API")
                                .description("API de Locações de veiculos")
                                .version("v1")
                                .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                                .contact(new Contact().name("Robson Ferreira").email("robson-ferreiradasilva@hotmail.com"))
                );
    }
}
