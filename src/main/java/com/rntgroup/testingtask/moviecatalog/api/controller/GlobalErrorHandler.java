package com.rntgroup.testingtask.moviecatalog.api.controller;

import java.time.OffsetDateTime;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.ApiError;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.ApiError.ErrorRecord;
import com.rntgroup.testingtask.moviecatalog.domain.exception.ResourceFieldException;
import com.rntgroup.testingtask.moviecatalog.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * It deals with all the errors that occur in the application.
 */
@Component
@RequiredArgsConstructor
public class GlobalErrorHandler {

    private final MessageSource messageSource;

    /**
     * Converts the given throwable to {@link ApiError}.
     *
     * @param e the given throwable
     * @return converted {@link ApiError}.
     */
    public ApiError convert(Throwable e) {
        return switch (e) {
            case ResourceNotFoundException exception ->
                    createApiErrorFrom(getMessage("exception.resource.notFound.message"), exception);
            case ResourceFieldException exception ->
                    createApiErrorFrom(getMessage("exception.resource.field.message"), exception);
            case null, default ->
                    new ApiError(getMessage("exception.common.message"), OffsetDateTime.now(), null);
        };
    }

    private ApiError createApiErrorFrom(String message, ResourceFieldException exception) {
        var reason = getMessage(exception.getMessageCode()) + exception.getFieldValue();
        var errorRecord = new ErrorRecord(exception.getFieldName(), reason);
        return new ApiError(message, OffsetDateTime.now(), errorRecord);
    }

    private String getMessage(String messageCode) {
        var locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageCode, null, locale);
    }
}
