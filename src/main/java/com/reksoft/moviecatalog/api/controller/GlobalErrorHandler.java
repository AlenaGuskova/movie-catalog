package com.reksoft.moviecatalog.api.controller;

import java.time.OffsetDateTime;

import com.reksoft.moviecatalog.api.response.dto.ApiError;
import com.reksoft.moviecatalog.api.response.dto.ApiError.ErrorRecord;
import com.reksoft.moviecatalog.common.local.LocalMessageService;
import com.reksoft.moviecatalog.domain.exception.ResourceFieldException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * It deals with all the errors that occur in the application.
 */
@Component
@RequiredArgsConstructor
public class GlobalErrorHandler {

    private final LocalMessageService localMessageService;

    /**
     * Converts the given throwable to {@link ApiError}.
     *
     * @param e the given throwable
     * @return converted {@link ApiError}.
     */
    public ApiError convert(Throwable e) {
        return switch (e) {
            case ResourceFieldException exception ->
                createApiErrorFrom(exception);
            case null, default ->
                new ApiError(localMessageService.getMessage("exception.common.message"),
                    OffsetDateTime.now(), null);
        };
    }

    private ApiError createApiErrorFrom(ResourceFieldException exception) {
        var reason = localMessageService.getMessage(exception.getReason())
                         + exception.getFieldValue();
        var errorRecord = new ErrorRecord(exception.getFieldName(), reason);
        return new ApiError(localMessageService.getMessage(exception.getMessage()),
            OffsetDateTime.now(), errorRecord);
    }
}
