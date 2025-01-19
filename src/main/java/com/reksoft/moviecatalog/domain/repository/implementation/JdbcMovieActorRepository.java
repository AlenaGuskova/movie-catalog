package com.reksoft.moviecatalog.domain.repository.implementation;

import com.reksoft.moviecatalog.domain.repository.MovieActorRepository;
import com.reksoft.moviecatalog.domain.exception.DataAccessException;
import com.reksoft.moviecatalog.domain.model.MovieActor;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * {@inheritDoc}
 */
@Repository
@RequiredArgsConstructor
public class JdbcMovieActorRepository implements MovieActorRepository {

    private final NamedParameterJdbcOperations operations;
    private final RowMapper<MovieActor> movieActorRowMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteMovieActors(String movieId) {
        var parameters = Map.of("movie_id", movieId);
        var query = "DELETE FROM movie_actor WHERE movie_id = :movie_id";
        return Try.of(() -> operations.update(query, parameters))
                   .getOrElseThrow(_ -> new DataAccessException("deleting movie actors by movie id: ".concat(movieId)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MovieActor> getMovieActors() {
        var query = "SELECT movie_id, actor_id FROM movie_actor";
        return Try.of(() -> operations.query(query, movieActorRowMapper))
                   .getOrElseThrow(_ -> new DataAccessException("getting movie actors"));
    }
}
