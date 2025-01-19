package com.reksoft.moviecatalog.api.response.dto;

import java.time.OffsetDateTime;

/**
 * A class representing an error that may occur during request processing.
 * Here, message is an error description;
 * dateOccurred the date and time when the error occurred.
 */
public record ApiError(String message, OffsetDateTime dateOccurred,
                       ErrorRecord error) {

    /**
     * A class representing an error details on the API request fields.
     *
     * @param fieldName API request fields that caused the error.
     * @param reason    message details on the field error.
     */
    public record ErrorRecord(String fieldName, String reason) {
    }
}
