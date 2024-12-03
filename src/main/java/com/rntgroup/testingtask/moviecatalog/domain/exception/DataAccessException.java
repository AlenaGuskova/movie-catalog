package com.rntgroup.testingtask.moviecatalog.domain.exception;

/**
 * Indicates an exception that occurs when accessing the database.
 */
public class DataAccessException extends RuntimeException {

    /**
     * {@inheritDoc}
     */
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
