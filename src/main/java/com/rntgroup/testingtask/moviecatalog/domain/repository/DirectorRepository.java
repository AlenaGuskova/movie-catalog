package com.rntgroup.testingtask.moviecatalog.domain.repository;

import com.rntgroup.testingtask.moviecatalog.domain.exception.DataAccessException;
import com.rntgroup.testingtask.moviecatalog.domain.exception.ResourceNotFoundException;
import com.rntgroup.testingtask.moviecatalog.domain.model.Director;

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
