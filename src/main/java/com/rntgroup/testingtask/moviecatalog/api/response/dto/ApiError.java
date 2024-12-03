package com.rntgroup.testingtask.moviecatalog.api.response.dto;

import lombok.Value;

import java.time.OffsetDateTime;

/**
 * A class representing an error that may occur during request processing.
 * Here, message is an error description;
 * dateOccurred the date and time when the error occurred.
 */
@Value
public class ApiError {
    String message;
    OffsetDateTime dateOccurred;
}
