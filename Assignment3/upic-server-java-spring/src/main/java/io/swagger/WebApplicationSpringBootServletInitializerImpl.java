package io.swagger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "io.swagger")
public class WebApplicationSpringBootServletInitializerImpl extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(WebApplicationSpringBootServletInitializerImpl.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebApplicationSpringBootServletInitializerImpl.class);
    }
}
