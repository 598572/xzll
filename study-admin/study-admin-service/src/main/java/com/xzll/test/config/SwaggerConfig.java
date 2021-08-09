package com.xzll.test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.plugin.core.SimplePluginRegistry;
import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.util.ArrayList;


@Configuration
@Import({DocConfig.class})
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private Docket docket;

    @PostConstruct
    private void apiInfo() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("study-test模块")
                .build();
        docket.apiInfo(apiInfo);
    }


}

