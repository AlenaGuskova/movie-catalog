package com.rntgroup.testingtask.moviecatalog.domain.repository.implementation;

import com.rntgroup.testingtask.moviecatalog.domain.exception.ResourceNotFoundException;
import com.rntgroup.testingtask.moviecatalog.domain.model.Director;
import com.rntgroup.testingtask.moviecatalog.domain.repository.DirectorRepository;
import com.rntgroup.testingtask.moviecatalog.domain.rowwrapper.DirectorRowWrapper;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@inheritDoc}
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcDirectorRepository implements DirectorRepository {

    private final JdbcOperations operations;
    private final DirectorRowWrapper directorRowWrapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public Director findById(String id) {
        log.debug("Finding director by id:'{}' from the database", id);
        var query = "SELECT id, first_name, last_name FROM director d WHERE d.id = ?";
        return Try.of(() -> operations.query(query, directorRowWrapper, id))
                .onFailure(e -> log.warn("Exception occurred when finding director by id: '{}' from the database", id, e))
                .mapTry(List::getFirst)
                .onFailure(_ -> log.warn("Director not found by id:'{}' from the database", id))
                .onSuccess(_ -> log.debug("Director found by id:'{}' in the database successfully", id))
                .getOrElseThrow(() -> new ResourceNotFoundException(id));
    }
}
