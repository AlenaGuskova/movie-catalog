package com.rntgroup.testingtask.moviecatalog.api.response;

import com.rntgroup.testingtask.moviecatalog.api.response.dto.ApiError;
import lombok.ToString;

@ToString
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
