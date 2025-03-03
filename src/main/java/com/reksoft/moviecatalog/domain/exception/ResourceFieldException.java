package com.reksoft.moviecatalog.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResourceFieldException extends RuntimeException {

    private final String fieldName;
    private final String fieldValue;
    private final String reason;
    private final String message;
}
