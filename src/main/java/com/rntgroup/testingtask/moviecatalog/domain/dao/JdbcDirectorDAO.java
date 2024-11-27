package com.rntgroup.testingtask.moviecatalog.domain.dao;

import com.rntgroup.testingtask.moviecatalog.domain.model.DirectorEntity;
import com.rntgroup.testingtask.moviecatalog.domain.wrapper.DirectorWrapper;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static io.vavr.API.$;
import static io.vavr.API.Case;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcDirectorDAO implements DAO<DirectorEntity> {

    private static final String SQL_QUERY_FIND_DIRECTOR_BY_ID =
            "SELECT d.* FROM director d WHERE d.id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final DirectorWrapper directorWrapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<DirectorEntity> findAll() {
        return List.of();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DirectorEntity findById(String id) {
        log.info("Finding director by id: {} from the database", id);
        var results = Try.of(() -> jdbcTemplate.query(SQL_QUERY_FIND_DIRECTOR_BY_ID,
                        directorWrapper, id))
                .onFailure(e -> log.warn("Error while getting director by id: {} from the database", id, e))
                .mapFailure(Case($(), e -> new DAOException("Couldn't find director by id " + id, e)))
                .get();
        if (results.isEmpty()) throw new EntityNotException(id);
        return results.getFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DirectorEntity save(DirectorEntity director) {
        return new DirectorEntity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DirectorEntity update(String id, DirectorEntity entity) {
        return new DirectorEntity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int delete(String id) {
        return 0;
    }
}
