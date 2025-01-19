package com.reksoft.moviecatalog.domain.repository;

import java.util.List;
import java.util.UUID;
import com.reksoft.moviecatalog.domain.exception.DataAccessException;
import com.reksoft.moviecatalog.domain.exception.ResourceNotFoundException;
import com.reksoft.moviecatalog.domain.model.Genre;

/**
 * DB repository for {@link Genre}.
 */
public interface GenreRepository {

    /**
     * Finds all genres.
     *
     * @return collection of genres.
     * @throws DataAccessException when dao access exception happens.
     */
    List<Genre> findAll();

    /**
     * Finds genres by a movie identifier.
     *
     * @param movieId movie identifier.
     * @return found genres.
     * @throws DataAccessException       when dao access exception happens.
     * @throws ResourceNotFoundException when genres were not found by movie identifier.
     */
    List<Genre> findByMovieId(UUID movieId);
}
