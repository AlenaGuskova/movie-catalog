package com.rntgroup.testingtask.moviecatalog.api.request;

import com.rntgroup.testingtask.moviecatalog.domain.model.Movie;
import lombok.Builder;
import lombok.Value;

/**
 * Data from request to update the movie {@link Movie}.
 */
@Value
public class UpdateMovieRequest {

    MovieRequest resource;

    @Value
    @Builder
    public static class MovieRequest {
        String id;
        String title;
        String directorId;
    }
}
