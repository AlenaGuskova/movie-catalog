package com.rntgroup.testingtask.moviecatalog.domain.repository.implementation;

import com.rntgroup.testingtask.moviecatalog.domain.exception.ResourceNotFoundException;
import com.rntgroup.testingtask.moviecatalog.domain.model.Director;
import com.rntgroup.testingtask.moviecatalog.domain.repository.DirectorRepository;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * {@inheritDoc}
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcDirectorRepository implements DirectorRepository {

    private final NamedParameterJdbcOperations operations;
    private final RowMapper<Director> directorRowMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public Director findById(String id) {
        log.debug("Finding director by id:'{}' from the database", id);
        var query = "SELECT id, first_name, last_name FROM director d WHERE d.id = :id";
        var parameters = Map.of("id", id);
        return Try.of(() -> operations.queryForObject(query, parameters, directorRowMapper))
                .onFailure(e -> log.warn("Exception occurred when finding director by id: '{}' from the database", id, e))
                .onFailure(e -> log.warn("Director not found by id:'{}' from the database", id, e))
                .onSuccess(_ -> log.debug("Director found by id:'{}' in the database successfully", id))
                .getOrElseThrow(() -> new ResourceNotFoundException(id));
    }
}
