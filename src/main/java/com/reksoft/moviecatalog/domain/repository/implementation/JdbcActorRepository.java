package com.reksoft.moviecatalog.domain.repository.implementation;

import com.reksoft.moviecatalog.domain.model.Actor;
import com.reksoft.moviecatalog.domain.repository.ActorRepository;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
@Repository
@RequiredArgsConstructor
public class JdbcActorRepository implements ActorRepository {

    private final NamedParameterJdbcOperations operations;
    private final RowMapper<Actor> actorRowMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Actor> findAll() {
        var query = "SELECT id, first_name, last_name FROM actor a";
        return Try.of(() -> operations.query(query, actorRowMapper))
                   .getOrNull();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Actor> findByMovieId(UUID movieId) {
        var query = """
            SELECT id, first_name, last_name
            FROM actor a
            JOIN movie_actor ma ON ma.actor_id = a.id
            WHERE ma.movie_id = :movieId
            """;
        return Try.of(() -> operations.query(query, Map.of("movieId", movieId), actorRowMapper))
                   .filter(actors -> !actors.isEmpty())
                   .getOrNull();
    }
}
