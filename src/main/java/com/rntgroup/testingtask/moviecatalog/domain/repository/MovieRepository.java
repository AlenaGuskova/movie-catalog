package com.rntgroup.testingtask.moviecatalog.domain.repository;

import java.util.Collection;
import java.util.List;
import com.rntgroup.testingtask.moviecatalog.domain.exception.DataAccessException;
import com.rntgroup.testingtask.moviecatalog.domain.exception.ResourceNotFoundException;
import com.rntgroup.testingtask.moviecatalog.domain.model.Movie;

/**
 * DB repository for {@link Movie}.
 */
public interface MovieRepository {

    /**
     * Finds all movies.
     *
     * @return collection of movies.
     * @throws DataAccessException when dao access exception happens.
     */
    Collection<Movie> findAll();

    /**
     * Finds a movie by its identifier.
     *
     * @param id identifier.
     * @return found movie.
     * @throws DataAccessException       when dao access exception happens.
     * @throws ResourceNotFoundException when movie was not found by its identifier.
     */
    Movie findById(String id);

    /**
     * Saves the given movie in the database.
     *
     * @param movie to be saved in the database.
     * @return saved movie.
     * @throws DataAccessException when dao access exception happens.
     */
    Movie save(Movie movie);

    /**
     * Updates the given movie in the database.
     *
     * @param movie to be updated in the database.
     * @return updated movie.
     * @throws DataAccessException       when dao access exception happens.
     * @throws ResourceNotFoundException when movie was not found by its identifier.
     */
    Movie update(Movie movie);

    /**
     * Deletes the given movie in the database.
     *
     * @param id identifier
     * @return row number deleted.
     * @throws DataAccessException       when dao access exception happens.
     * @throws ResourceNotFoundException when movie was not found by its identifier.
     */
    int delete(String id);

    /**
     * Finds list of movies by genre title.
     *
     * @param genre genre title in lower case.
     * @return collection of found movies.
     */
    List<Movie> findByGenreIgnoreCase(String genre);

    /**
     * Finds list of movies by first name or last name of movie's director or an actor.
     *
     * @param name first name or last name of movie's director or an actor in lower case.
     * @return collection of found movies.
     */
    List<Movie> findByNameIgnoreCase(String name);

    /**
     * Finds list of movies by string in the title.
     *
     * @param prefix string that can be in the movie title in lower case.
     * @return collection of found movies.
     */
    List<Movie> findByPrefixInTitleIgnoreCase(String prefix);
}
