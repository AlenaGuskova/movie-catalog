package com.rntgroup.testingtask.moviecatalog.domain.dao;

public class EntityNotException extends RuntimeException {
    public EntityNotException(String id) {
        super("Couldn't find entity by id: " + id);
    }
}
