package com.takuhome.back.util.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author nekotaku
 * @create 2021-09-16 9:52
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.takuhome.back.controller"))
                .paths(PathSelectors.any())
                //enable如果为false，则swagger就不能在浏览器中访问
                //enable的应用就主要在于，生产环境中使用swagger，发布时就不使用swagger，那么其实我们只用
                // 去改变这个enable的值就可以了

                .build();

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("猫の屋后台管理")
                .description("后台管理系统API")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();

    }
}
