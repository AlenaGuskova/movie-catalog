package com.reksoft.moviecatalog.domain.repository;

import com.reksoft.moviecatalog.domain.exception.DataAccessException;
import com.reksoft.moviecatalog.domain.exception.ResourceNotFoundException;
import com.reksoft.moviecatalog.domain.model.Director;

/**
 * DB repository for {@link Director}.
 */
public interface DirectorRepository {

    /**
     * Finds a director by their identifier.
     *
     * @param id identifier.
     * @return found director.
     * @throws DataAccessException       when dao access exception happens.
     * @throws ResourceNotFoundException when director was not found by its identifier.
     */
    Director findById(String id);
}
