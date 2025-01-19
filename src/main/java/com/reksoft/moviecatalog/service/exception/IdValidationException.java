package com.reksoft.moviecatalog.service.exception;

import com.reksoft.moviecatalog.domain.exception.ResourceFieldException;

public class IdValidationException extends ResourceFieldException {

    public IdValidationException(String fieldValue) {
        super("id", fieldValue, "exception.resource.id.uuid", "exception.resource.field.message");
    }
}
