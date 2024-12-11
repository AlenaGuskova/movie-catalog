package com.rntgroup.testingtask.moviecatalog.service.exception;

import com.rntgroup.testingtask.moviecatalog.domain.exception.ResourceFieldException;

public class IdValidationException extends ResourceFieldException {

    public IdValidationException(String fieldValue) {
        super("id", fieldValue, "exception.resource.id.uuid");
    }
}
