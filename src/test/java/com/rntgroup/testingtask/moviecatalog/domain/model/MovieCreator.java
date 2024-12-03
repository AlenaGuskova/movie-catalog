package com.rntgroup.testingtask.moviecatalog.domain.model;

import com.rntgroup.testingtask.moviecatalog.api.response.dto.ActorDto;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.DirectorDto;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.GenreDto;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.MovieDto;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class MovieCreator {

    public static Movie createMovie(String id, String title) {
        return new Movie()
                .setId(id)
                .setTitle(title);
    }

    public static Movie createMovie(String id, String title,
                                    Director director,
                                    List<Genre> genres,
                                    List<Actor> actors) {
        return new Movie()
                .setId(id)
                .setTitle(title)
                .setGenres(genres)
                .setDirector(director)
                .setActors(actors);
    }

    public static MovieDto createMovieDto(String id, String title,
                                          DirectorDto directorDto,
                                          List<GenreDto> genres,
                                          List<ActorDto> actors) {
        return MovieDto.builder()
                .id(id)
                .title(title)
                .director(directorDto)
                .genres(genres)
                .actors(actors)
                .build();
    }

    public static MovieDto createMovieDto(String id, String title) {
        return MovieDto.builder()
                .id(id)
                .title(title)
                .build();
    }
}