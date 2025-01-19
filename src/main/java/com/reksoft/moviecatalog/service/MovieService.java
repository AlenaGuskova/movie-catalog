package com.reksoft.moviecatalog.service;

import com.reksoft.moviecatalog.api.request.CreateMovieRequest;
import com.reksoft.moviecatalog.api.request.UpdateMovieRequest;
import com.reksoft.moviecatalog.api.response.dto.MovieDto;
import com.reksoft.moviecatalog.domain.model.Movie;

import java.util.Collection;

/**
 * Service to interact with {@link Movie}.
 */
public interface MovieService {

    /**
     * Gets all movies.
     *
     * @return collection of {@link MovieDto} when it found an empty list otherwise.
     */
    Collection<MovieDto> getMovies();

    /**
     * Gets list of movies by genre title.
     *
     * @param genreTitle genre title.
     * @return collection of {@link MovieDto} when it found an empty list otherwise.
     */
    Collection<MovieDto> getByGenre(String genreTitle);

    /**
     * Gets list of movies by first name or last name of movie's director or an actor.
     *
     * @param name first name or last name of movie's director or an actor.
     * @return collection of found movies.
     */
    Collection<MovieDto> getByName(String name);

    /**
     * Finds list of movies by prefix in the title.
     *
     * @param prefix string that can be in the movie title.
     * @return collection of {@link MovieDto} when it found an empty list otherwise.
     */
    Collection<MovieDto> getByPrefixInTitle(String prefix);

    /**
     * Finds {@link MovieDto} by its identifier.
     *
     * @param id identifier
     * @return {@link MovieDto} when it found an empty movie otherwise.
     */
    MovieDto getById(String id);

    /**
     * Deletes {@link MovieDto} by its identifier.
     *
     * @return row number deleted.
     */
    int delete(String id);

    /**
     * Creates the movie in the data store.
     *
     * @param request movie data to create movie.
     * @return {@link MovieDto} when it created an empty movie otherwise.
     */
    MovieDto create(CreateMovieRequest request);

    /**
     * Updates the movie in the data store.
     *
     * @param request movie data to update a movie.
     * @return {@link MovieDto} when it updated an empty movie otherwise.
     */
    MovieDto update(UpdateMovieRequest request);
}
