package com.rntgroup.testingtask.moviecatalog.configuration;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.BaseNCodec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * General application configuration.
 *
 * @author Alena_Guskova.
 */
@Configuration
class ApplicationConfiguration {

    @Bean
    Random random() throws NoSuchAlgorithmException {
        return SecureRandom.getInstanceStrong();
    }

    @Bean
    BaseNCodec encoder() {
        return new Base32();
    }
}
