package com.rntgroup.testingtask.moviecatalog.service;

import com.rntgroup.testingtask.moviecatalog.api.response.dto.Movie;

import java.util.Collection;

public interface MovieService {

    /**
     * Gets all movies.
     *
     * @return collection of {@link Movie} when it found an empty list otherwise.
     */
    Collection<Movie> listMovies();

    /**
     * Gets list of movies by genre title.
     *
     * @param genreTitle genre title.
     * @return collection of {@link Movie} when it found an empty list otherwise.
     */
    Collection<Movie> getByGenre(String genreTitle);

    /**
     * Gets list of movies by first name or last name of movie's director or an actor.
     *
     * @param name first name or last name of movie's director or an actor.
     * @return collection of found movies.
     */
    Collection<Movie> getByName(String name);

    /**
     * Finds list of movies by prefix in the title.
     *
     * @param prefix string that can be in the movie title.
     * @return collection of {@link Movie} when it found an empty list otherwise.
     */
    Collection<Movie> getByPrefixInTitle(String prefix);

    /**
     * Finds {@link Movie} by its identifier.
     *
     * @param id identifier
     * @return {@link Movie} when it found an empty movie otherwise.
     */
    Movie getById(String id);

    /**
     * Deletes {@link Movie} by its identifier.
     *
     * @return row number deleted.
     */
    int delete(String id);

    /**
     * Creates {@link Movie}.
     *
     * @param toCreate movie to be created.
     * @return {@link Movie} when it created an empty movie otherwise.
     */
    Movie create(Movie toCreate);

    /**
     * Updates {@link Movie}.
     *
     * @param id movie identifier.
     * @param toUpdate movie to be updated.
     * @return {@link Movie} when it updated an empty movie otherwise.
     */
    Movie update(String id, Movie toUpdate);
}
