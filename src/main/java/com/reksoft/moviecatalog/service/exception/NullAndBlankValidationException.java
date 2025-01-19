package com.reksoft.moviecatalog.service.exception;

import com.reksoft.moviecatalog.domain.exception.ResourceFieldException;

public class NullAndBlankValidationException extends ResourceFieldException {

    public NullAndBlankValidationException(String fieldName, String fieldValue) {
        super(fieldName, fieldValue, "exception.resource.field.notBlank", "exception.resource.field.message");
    }
}
