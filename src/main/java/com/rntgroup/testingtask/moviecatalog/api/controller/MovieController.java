package com.rntgroup.testingtask.moviecatalog.api.controller;

import com.rntgroup.testingtask.moviecatalog.api.response.Response;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.ApiError;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.Movie;

import java.util.Collection;

public interface MovieController {

    /**
     * Gets all movies.
     *
     * @return collection of {@link Movie} when it found an empty list otherwise.
     * When something went wrong it returns {@link ApiError}.
     */
    Response<Collection<Movie>> listMovies();

    /**
     * Gets list of movies by genre title.
     *
     * @param genreTitle genre title.
     * @return collection of {@link Movie} when it found an empty list otherwise.
     * When something went wrong it returns {@link ApiError}.
     */
    Response<Collection<Movie>> getByGenre(String genreTitle);

    /**
     * Gets list of movies by first name or last name of movie's director or an actor.
     *
     * @param name first name or last name of movie's director or an actor.
     * @return collection of {@link Movie} when it found an empty list otherwise.
     * When something went wrong it returns {@link ApiError}.
     */
    Response<Collection<Movie>> getByName(String name);

    /**
     * Gets list of movies by prefix in the title.
     *
     * @param prefix string that can be in the movie title.
     * @return collection of {@link Movie} when it found an empty list otherwise.
     * When something went wrong it returns {@link ApiError}.
     */
    Response<Collection<Movie>> getByPrefixInTitle(String prefix);

    /**
     * Gets {@link Movie} by its identifier.
     *
     * @param id identifier
     * @return {@link Movie} when it found an empty movie otherwise.
     * When something went wrong it returns {@link ApiError}.
     */
    Response<Movie> getById(String id);

    /**
     * Deletes {@link Movie} by its identifier.
     *
     * @return row number deleted.
     * When something went wrong it returns {@link ApiError}.
     */
    Response<Integer> delete(String id);

    /**
     * Creates {@link Movie}.
     *
     * @param title      movie title
     * @param directorId movie director identifier.
     * @return created movie.
     * When something went wrong it returns {@link ApiError}.
     */
    Response<Movie> create(String title, String directorId);

    /**
     * Updates {@link Movie}.
     *
     * @param id         movie identifier.
     * @param title      movie title
     * @param directorId movie director identifier.
     * @return created movie.
     * When something went wrong it returns {@link ApiError}.
     */
    Response<Movie> update(String id, String title, String directorId);
}
