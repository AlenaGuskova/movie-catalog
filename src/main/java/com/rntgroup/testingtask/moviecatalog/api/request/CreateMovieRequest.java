package com.rntgroup.testingtask.moviecatalog.api.request;

import com.rntgroup.testingtask.moviecatalog.domain.model.Movie;

/**
 * Data from request to create a new movie {@link Movie}.
 */
public record CreateMovieRequest(MovieRequest movie) {

    public record MovieRequest(String id, String title, String directorId) {
    }
}
