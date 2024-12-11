package com.rntgroup.testingtask.moviecatalog.api.controller;

import com.rntgroup.testingtask.moviecatalog.api.response.Response;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.ApiError;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.MovieDto;

import java.util.Collection;

/**
 * API resource to work with movies.
 */
public interface MovieController {

    /**
     * Gets all movies.
     *
     * @return collection of {@link MovieDto} when it found an empty list otherwise.
     * When something went wrong it returns {@link ApiError}.
     */
    Response<Collection<MovieDto>> getMovies();

    /**
     * Gets list of movies by genre title.
     *
     * @param genreTitle genre title.
     * @return collection of {@link MovieDto} when it found an empty list otherwise.
     * When something went wrong it returns {@link ApiError}.
     */
    Response<Collection<MovieDto>> getByGenre(String genreTitle);

    /**
     * Gets list of movies by first name or last name of movie's director or an actor.
     *
     * @param name first name or last name of movie's director or an actor.
     * @return collection of {@link MovieDto} when it found an empty list otherwise.
     * When something went wrong it returns {@link ApiError}.
     */
    Response<Collection<MovieDto>> getByName(String name);

    /**
     * Gets list of movies by prefix in the title.
     *
     * @param prefix string that can be in the movie title.
     * @return collection of {@link MovieDto} when it found an empty list otherwise.
     * When something went wrong it returns {@link ApiError}.
     */
    Response<Collection<MovieDto>> getByPrefixInTitle(String prefix);

    /**
     * Gets {@link MovieDto} by its identifier.
     *
     * @param id identifier
     * @return {@link MovieDto} when it found an empty movie otherwise.
     * When something went wrong it returns {@link ApiError}.
     */
    Response<MovieDto> getById(String id);

    /**
     * Deletes {@link MovieDto} by its identifier.
     *
     * @return row number deleted.
     * When something went wrong it returns {@link ApiError}.
     */
    Response<Integer> delete(String id);

    /**
     * Creates {@link MovieDto}.
     *
     * @param title      movie title
     * @param directorId movie director identifier.
     * @return created movie.
     * When something went wrong it returns {@link ApiError}.
     */
    Response<MovieDto> create(String title, String directorId);

    /**
     * Updates {@link MovieDto}.
     *
     * @param id         movie identifier.
     * @param title      movie title
     * @param directorId movie director identifier.
     * @return created movie.
     * When something went wrong it returns {@link ApiError}.
     */
    Response<MovieDto> update(String id, String title, String directorId);
}
