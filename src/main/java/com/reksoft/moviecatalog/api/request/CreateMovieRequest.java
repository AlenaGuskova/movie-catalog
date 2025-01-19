package com.reksoft.moviecatalog.api.request;

import com.reksoft.moviecatalog.domain.model.Movie;

/**
 * Data from request to create a new movie {@link Movie}.
 */
public record CreateMovieRequest(MovieRequest movie) {

    public record MovieRequest(String id, String title, String directorId) {
    }
}
