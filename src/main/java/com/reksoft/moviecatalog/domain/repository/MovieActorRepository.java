package com.reksoft.moviecatalog.domain.repository;

import java.util.Collection;
import com.reksoft.moviecatalog.domain.exception.DataAccessException;
import com.reksoft.moviecatalog.domain.exception.ResourceNotFoundException;
import com.reksoft.moviecatalog.domain.model.MovieActor;

/**
 * DB repository for {@link MovieActor}.
 */
public interface MovieActorRepository {

    /**
     * Deletes actors of the given movie in the database.
     *
     * @param movieId movie identifier
     * @return row number deleted.
     * @throws DataAccessException       when dao access exception happens.
     * @throws ResourceNotFoundException when movie was not found by its identifier.
     */
    int deleteMovieActors(String movieId);

    /**
     * Finds all movie actors.
     *
     * @return collection of movie actors.
     * @throws DataAccessException when dao access exception happens.
     */
    Collection<MovieActor> getMovieActors();
}
