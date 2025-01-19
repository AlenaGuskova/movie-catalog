package com.reksoft.moviecatalog.api.response;

import com.reksoft.moviecatalog.api.response.dto.ApiError;

/**
 * Response to a request containing content or an error {@link ApiError}.
 *
 * @param <T> content representing the domain.
 */
public record Response<T>(T content, ApiError apiError) {

    public static <T> Response<T> success(T value) {
        return new Response<>(value, null);
    }

    public static <T> Response<T> failure(ApiError apiError) {
        return new Response<>(null, apiError);
    }
}
