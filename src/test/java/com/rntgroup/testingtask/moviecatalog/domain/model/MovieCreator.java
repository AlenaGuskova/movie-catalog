package com.rntgroup.testingtask.moviecatalog.domain.model;

import java.util.List;
import java.util.UUID;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.ActorDto;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.DirectorDto;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.GenreDto;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.MovieDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MovieCreator {

    public static Movie createMovie(String id, String title) {
        return createMovie(id, title, null, null, null);
    }

    public static Movie createMovie(String id, String title,
                                    Director director,
                                    List<Genre> genres,
                                    List<Actor> actors) {
        return new Movie()
                .setId(UUID.fromString(id))
                .setTitle(title)
                .setGenres(genres)
                .setDirector(director)
                .setActors(actors);
    }

    public static MovieDto createMovieDto(String id, String title,
                                          DirectorDto director,
                                          List<GenreDto> genres,
                                          List<ActorDto> actors) {
        return new MovieDto(id, title, director, genres, actors);
    }

    public static MovieDto createMovieDto(String id, String title) {
        return createMovieDto(id, title, null, null, null);
    }

    public static MovieDto createMovieDto(String id, String title, DirectorDto director) {
        return createMovieDto(id, title, director, null, null);
    }
}