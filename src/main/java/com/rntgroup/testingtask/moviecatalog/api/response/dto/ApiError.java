package com.rntgroup.testingtask.moviecatalog.api.response.dto;

import java.time.OffsetDateTime;

public record ApiError(String message, OffsetDateTime dateOccurred) {
}
