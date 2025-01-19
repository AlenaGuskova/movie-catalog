package com.reksoft.moviecatalog.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * General application configuration.
 */
@Configuration
public class ApplicationConfiguration {

    /**
     * Creates object mapper bean.
     *
     * @return {@link ObjectMapper}.
     */
    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper()
                   .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                   .enable(SerializationFeature.INDENT_OUTPUT)
                   .registerModule(new JavaTimeModule());
    }
}
