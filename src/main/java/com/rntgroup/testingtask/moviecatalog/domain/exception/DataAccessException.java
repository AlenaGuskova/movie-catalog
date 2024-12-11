package com.rntgroup.testingtask.moviecatalog.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Indicates an exception that occurs when accessing the database.
 */
@Getter
@RequiredArgsConstructor
public class DataAccessException extends RuntimeException {

    private final String operationName;
}
