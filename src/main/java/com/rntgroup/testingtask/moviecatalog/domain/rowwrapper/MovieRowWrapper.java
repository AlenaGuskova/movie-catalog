package com.rntgroup.testingtask.moviecatalog.domain.rowwrapper;

import com.rntgroup.testingtask.moviecatalog.domain.model.Movie;
import io.vavr.control.Try;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

/**
 * {@inheritDoc}
 */
@Component
public class MovieRowWrapper implements RowMapper<Movie> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie mapRow(ResultSet rs, int i) {
        var id = getOrNull(rs, "id");
        var title = getOrNull(rs, "title");
        return new Movie()
                .setId(id)
                .setTitle(title);
    }

    private String getOrNull(ResultSet rs, String name) {
        return Try.of(() -> rs.getString(name))
                .getOrNull();
    }
}
