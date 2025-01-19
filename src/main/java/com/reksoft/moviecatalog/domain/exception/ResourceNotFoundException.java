package com.reksoft.moviecatalog.domain.exception;

/**
 * Indicates that a resource has not been found.
 */
public class ResourceNotFoundException extends ResourceFieldException {

    public ResourceNotFoundException(String resourceId) {
        super("id", resourceId, "exception.resource.notFound", "exception.resource.notFound.message");
    }
}
