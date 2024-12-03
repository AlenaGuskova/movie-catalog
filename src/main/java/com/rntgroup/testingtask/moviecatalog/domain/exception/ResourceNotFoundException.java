package com.rntgroup.testingtask.moviecatalog.domain.exception;

/**
 * Indicates that a resource has not been found.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param id the identifier of the resource that could not be found.
     */
    public ResourceNotFoundException(String id) {
        super("Could not find the resource by identifier: " + id);
    }
}
