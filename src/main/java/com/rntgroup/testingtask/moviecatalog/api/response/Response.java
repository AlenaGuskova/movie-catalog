package com.rntgroup.testingtask.moviecatalog.api.response;

import com.rntgroup.testingtask.moviecatalog.api.response.dto.ApiError;
import lombok.Getter;
import lombok.ToString;

/**
 * Response to a request containing content or an error {@link ApiError}.
 *
 * @param <T> content representing the domain.
 */
@ToString
@Getter
public class Response<T> {
    private final T content;
    private final ApiError apiError;

    private Response(T content, ApiError apiError) {
        this.content = content;
        this.apiError = apiError;
    }

    public static <T> Response<T> success(T value) {
        return new Response<>(value, null);
    }

    public static <T> Response<T> fail(ApiError apiError) {
        return new Response<>(null, apiError);
    }
}
