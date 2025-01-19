package com.reksoft.moviecatalog.api.request;

import com.reksoft.moviecatalog.domain.model.Movie;

/**
 * Data from request to update the movie {@link Movie}.
 */
public record UpdateMovieRequest(MovieRequest movie) {

    public record MovieRequest(String id, String title, String directorId) {
    }
}
