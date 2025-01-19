package com.reksoft.moviecatalog.domain.repository;

import java.util.List;
import java.util.UUID;
import com.reksoft.moviecatalog.domain.exception.DataAccessException;
import com.reksoft.moviecatalog.domain.exception.ResourceNotFoundException;
import com.reksoft.moviecatalog.domain.model.Actor;

/**
 * DB repository for {@link Actor}.
 */
public interface ActorRepository {

    /**
     * Finds all actors.
     *
     * @return collection of actors.
     * @throws DataAccessException when dao access exception happens.
     */
    List<Actor> findAll();

    /**
     * Finds actors by a movie identifier.
     *
     * @param movieId movie identifier.
     * @return found actors.
     * @throws DataAccessException       when dao access exception happens.
     * @throws ResourceNotFoundException when actors were not found by movie identifier.
     */
    List<Actor> findByMovieId(UUID movieId);
}
