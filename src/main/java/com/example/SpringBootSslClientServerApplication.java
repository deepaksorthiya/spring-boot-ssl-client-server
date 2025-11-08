package com.example;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
@ImportRuntimeHints(SpringBootSslClientServerApplication.ApplicationHints.class)
public class SpringBootSslClientServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSslClientServerApplication.class, args);
    }

    static class ApplicationHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            // register ssl files
            hints.resources().registerResource(new ClassPathResource("springbootssl.jks"));
            hints.resources().registerResource(new ClassPathResource("springbootssl.p12"));
        }
    }

}
