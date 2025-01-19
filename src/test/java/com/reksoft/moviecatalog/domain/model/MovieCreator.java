package com.reksoft.moviecatalog.domain.model;

import java.util.List;
import java.util.UUID;

import com.reksoft.moviecatalog.api.response.dto.DirectorDto;
import com.reksoft.moviecatalog.api.response.dto.MovieDto;
import com.reksoft.moviecatalog.domain.mapper.MovieMapperImpl;
import lombok.experimental.UtilityClass;

import static com.reksoft.moviecatalog.domain.model.ActorCreator.createActor;
import static com.reksoft.moviecatalog.domain.model.DirectorCreator.createDirector;
import static com.reksoft.moviecatalog.domain.model.GenreCreator.createGenre;

@UtilityClass
public class MovieCreator {

    private static final MovieMapperImpl MOVIE_MAPPER = new MovieMapperImpl();

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

    public static MovieDto createMovieDto(String id, String title) {
        return createMovieDto(id, title, null);
    }

    public static MovieDto createMovieDto(String id, String title, DirectorDto director) {
        return new MovieDto(id, title, director, null, null);
    }

    public static Movie getPopularMovie() {
        return createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Возвращение короля",
            createDirector("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"),
            List.of(createGenre("80bd39cc-e349-48fb-9c06-373fe9d04768", "фэнтези"),
                createGenre("27a33184-9500-4847-b83b-647de0538b2b", "приключения"),
                createGenre("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
            List.of(createActor("3f825e80-f070-4829-85ce-62964a02854a", "Элайджа", "Вуд"),
                createActor("4a42c006-c853-4795-b98e-3eefe6552940", "Вигго", "Мортенсен"))
        );
    }

    public static List<Movie> getMoviesFromDB() {
        var expected1 = createMovie("d890bbaf-f8e7-4c47-861b-e9368fabbd02", "1+1",
            createDirector("544e0aef-cb6f-413e-8e56-667c2f739e4d", "Оливье", "Накаш"),
            List.of(createGenre("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
            List.of(createActor("64887d99-ff1d-48da-b066-7dbeb0f464f3", "Франсуа", "Клюзе"),
                createActor("1abf568a-7106-49f1-b152-d002ba78df7d", "Омар", "Си")));
        var expected2 = getPopularMovie();
        var expected3 = createMovie("e74318b9-0b1b-41c9-8c89-9779ce0261f3", "Унесённые призраками");
        return List.of(expected1, expected2, expected3);
    }

    public static MovieDto getPopularMovieDto() {
        return MOVIE_MAPPER.toDto(getPopularMovie());
    }

    public static List<MovieDto> getMoviesDtoFromDB() {
        return getMoviesFromDB().stream()
                   .map(MOVIE_MAPPER::toDto)
                   .toList();
    }
}