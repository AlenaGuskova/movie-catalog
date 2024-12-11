package com.rntgroup.testingtask.moviecatalog.domain.rowmapper;

import com.rntgroup.testingtask.moviecatalog.domain.model.Actor;
import com.rntgroup.testingtask.moviecatalog.domain.model.Director;
import com.rntgroup.testingtask.moviecatalog.domain.model.Genre;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

@Configuration
public class RowMapperConfiguration {

    @Bean
    RowMapper<Director> directorRowMapper() {
        return new BeanPropertyRowMapper<>(Director.class);
    }

    @Bean
    RowMapper<Actor> actorRowMapper() {
        return new BeanPropertyRowMapper<>(Actor.class);
    }

    @Bean
    RowMapper<Genre> genreRowMapper() {
        return new BeanPropertyRowMapper<>(Genre.class);
    }
}
