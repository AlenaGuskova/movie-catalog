package com.rntgroup.testingtask.moviecatalog.api.request;

import com.rntgroup.testingtask.moviecatalog.domain.model.Movie;
import lombok.Builder;
import lombok.Value;

/**
 * Data from request to create a new movie {@link Movie}.
 */
@Value
public class CreateMovieRequest {

        MovieRequest resource;

    @Value
    @Builder(toBuilder = true)
    public static class MovieRequest {
        String id;
        String title;
        String directorId;
    }
}
