package com.rntgroup.testingtask.moviecatalog.domain.model;

import com.rntgroup.testingtask.moviecatalog.api.response.dto.Actor;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.Director;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.Genre;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.Movie;

import java.util.List;

public class MovieCreator {

    public static MovieEntity createMovieEntity(String id, String title,
                                                DirectorEntity director,
                                                List<GenreEntity> genres,
                                                List<ActorEntity> actors) {
        return new MovieEntity()
                .setId(id)
                .setTitle(title)
                .setGenres(genres)
                .setDirector(director)
                .setActors(actors);
    }

    public static Movie createMovie(String id, String title,
                                    Director director,
                                    List<Genre> genres,
                                    List<Actor> actors) {
        return Movie.builder()
                .id(id)
                .title(title)
                .director(director)
                .genres(genres)
                .actors(actors)
                .build();
    }
}