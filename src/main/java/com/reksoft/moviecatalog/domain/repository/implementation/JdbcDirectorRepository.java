package com.reksoft.moviecatalog.domain.repository.implementation;

import java.util.Map;

import com.reksoft.moviecatalog.domain.repository.DirectorRepository;
import com.reksoft.moviecatalog.domain.exception.ResourceNotFoundException;
import com.reksoft.moviecatalog.domain.model.Director;
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
public class JdbcDirectorRepository implements DirectorRepository {

    private final NamedParameterJdbcOperations operations;
    private final RowMapper<Director> directorRowMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public Director findById(String id) {
        var query = "SELECT id, first_name, last_name FROM director d WHERE d.id = :id";
        var parameters = Map.of("id", id);
        return Try.of(() -> operations.queryForObject(query, parameters, directorRowMapper))
                   .getOrElseThrow(() -> new ResourceNotFoundException(id));
    }
}
