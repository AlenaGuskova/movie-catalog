package com.reksoft.moviecatalog.domain.repository.implementation;

import com.reksoft.moviecatalog.domain.repository.MovieGenreRepository;
import com.reksoft.moviecatalog.domain.exception.DataAccessException;
import com.reksoft.moviecatalog.domain.model.MovieGenre;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

/**
 * {@inheritDoc}
 */
@Repository
@RequiredArgsConstructor
public class JdbcMovieGenreRepository implements MovieGenreRepository {

    private final NamedParameterJdbcOperations operations;
    private final RowMapper<MovieGenre> movieGenreRowMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteMovieGenres(String movieId) {
        var parameters = Map.of("movie_id", movieId);
        var query = "DELETE FROM movie_genre WHERE movie_id = :movie_id";
        return Try.of(() -> operations.update(query, parameters))
                   .getOrElseThrow(_ -> new DataAccessException("deleting movie genres by movie id: ".concat(movieId)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<MovieGenre> getMovieGenres() {
        var query = "SELECT movie_id, genre_id FROM movie_genre";
        return Try.of(() -> operations.query(query, movieGenreRowMapper))
                   .getOrElseThrow(_ -> new DataAccessException("deleting movie genres"));
    }
}
