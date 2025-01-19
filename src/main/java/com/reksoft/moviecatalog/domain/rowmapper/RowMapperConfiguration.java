package com.reksoft.moviecatalog.domain.rowmapper;

import com.reksoft.moviecatalog.domain.model.Actor;
import com.reksoft.moviecatalog.domain.model.Director;
import com.reksoft.moviecatalog.domain.model.Genre;
import com.reksoft.moviecatalog.domain.model.MovieActor;
import com.reksoft.moviecatalog.domain.model.MovieGenre;
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

    @Bean
    RowMapper<MovieActor> movieActorRowMapper() {
        return new BeanPropertyRowMapper<>(MovieActor.class);
    }

    @Bean
    RowMapper<MovieGenre> movieGenreRowMapper() {
        return new BeanPropertyRowMapper<>(MovieGenre.class);
    }
}
