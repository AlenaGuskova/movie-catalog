package com.rntgroup.testingtask.moviecatalog.domain.rowmapper;

import com.rntgroup.testingtask.moviecatalog.domain.model.Movie;
import io.vavr.control.Try;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
@Component
public class MovieRowMapper implements RowMapper<Movie> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie mapRow(ResultSet rs, int i) {
        return new Movie()
                .setId(getIdOrNull(rs))
                .setTitle(getOrNull(rs, "title"));
    }

    private UUID getIdOrNull(ResultSet rs) {
        return Try.of(() -> rs.getString("id"))
                .map(UUID::fromString)
                .getOrNull();
    }

    private String getOrNull(ResultSet rs, String name) {
        return Try.of(() -> rs.getString(name))
                .getOrNull();
    }
}
