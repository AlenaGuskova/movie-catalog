package com.rntgroup.testingtask.moviecatalog.service.exception;

import com.rntgroup.testingtask.moviecatalog.domain.exception.ResourceFieldException;

public class NullAndBlankValidationException extends ResourceFieldException {

    public NullAndBlankValidationException(String fieldName, String fieldValue) {
        super(fieldName, fieldValue, "exception.resource.field.notBlank");
    }
}
