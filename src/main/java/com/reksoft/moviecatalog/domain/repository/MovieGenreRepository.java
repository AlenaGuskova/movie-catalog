package com.reksoft.moviecatalog.domain.repository;

import com.reksoft.moviecatalog.domain.exception.DataAccessException;
import com.reksoft.moviecatalog.domain.model.MovieGenre;

import java.util.Collection;

/**
 * DB repository for {@link MovieGenre}.
 */
public interface MovieGenreRepository {

    /**
     * Deletes genres of the given movie in the database.
     *
     * @param movieId movie identifier
     * @return row number deleted.
     * @throws DataAccessException when dao access exception happens.
     */
    int deleteMovieGenres(String movieId);

    /**
     * Finds all movie genres.
     *
     * @return collection of movie genres.
     * @throws DataAccessException when dao access exception happens.
     */
    Collection<MovieGenre> getMovieGenres();
}
