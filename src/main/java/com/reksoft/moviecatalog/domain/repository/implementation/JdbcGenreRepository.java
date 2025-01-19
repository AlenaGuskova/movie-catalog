package com.reksoft.moviecatalog.domain.repository.implementation;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.reksoft.moviecatalog.domain.repository.GenreRepository;
import com.reksoft.moviecatalog.domain.model.Genre;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

/**
 * {@inheritDoc}
 */
@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations operations;
    private final RowMapper<Genre> genreRowMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Genre> findAll() {
        var query = "SELECT id, title FROM genre";
        return Try.of(() -> operations.query(query, genreRowMapper))
                   .filter(genres -> !genres.isEmpty())
                   .getOrNull();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Genre> findByMovieId(UUID movieId) {
        var query = """
            SELECT id, title
            FROM genre
            JOIN movie_genre mg ON mg.genre_id = genre.id
            WHERE mg.movie_id = :movieId
            """;
        return Try.of(() -> operations.query(query, Map.of("movieId", movieId), genreRowMapper))
                   .filter(genres -> !genres.isEmpty())
                   .getOrNull();
    }
}
