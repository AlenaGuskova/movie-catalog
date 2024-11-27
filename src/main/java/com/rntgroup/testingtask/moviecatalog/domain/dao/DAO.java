package com.rntgroup.testingtask.moviecatalog.domain.dao;

import java.util.Collection;

public interface DAO<T> {

    /**
     * Finds all entities.
     *
     * @return collection of entities.
     * @throws DAOException when dao access exception happens.
     */
    Collection<T> findAll();

    /**
     * Finds an entity by its identifier.
     *
     * @param id identifier
     * @return found entity
     * @throws DAOException when dao access exception happens.
     * @throws EntityNotException when entity was not found by its identifier.
     */
    T findById(String id);

    /**
     * Saves the given entity in the database.
     *
     * @param entity to be saved in the database.
     * @return saved entity.
     * @throws DAOException when dao access exception happens.
     */
    T save(T entity);

    /**
     * Updates the given entity in the database.
     *
     * @param id entity identifier.
     * @param entity to be updated in the database.
     * @return updated entity.
     * @throws DAOException when dao access exception happens.
     */
    T update(String id, T entity);

    /**
     * Deletes the given entity in the database.
     *
     * @param id identifier
     * @return row number deleted.
     * @throws DAOException when dao access exception happens.
     */
    int delete(String id);
}
