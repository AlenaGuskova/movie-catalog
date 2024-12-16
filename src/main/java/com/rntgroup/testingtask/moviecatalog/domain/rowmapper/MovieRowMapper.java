package com.rntgroup.testingtask.moviecatalog.domain.rowmapper;

import java.sql.ResultSet;
import java.util.UUID;
import com.rntgroup.testingtask.moviecatalog.domain.model.Director;
import com.rntgroup.testingtask.moviecatalog.domain.model.Movie;
import com.rntgroup.testingtask.moviecatalog.domain.model.Movie.Fields;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * {@inheritDoc}
 */
@Component
@RequiredArgsConstructor
public class MovieRowMapper implements RowMapper<Movie> {

    private final RowMapper<Director> directorRowMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie mapRow(ResultSet rs, int i) {
        var director = Try.of(() -> directorRowMapper.mapRow(rs, i))
                .filter(mappedDirector -> mappedDirector.getId() != null)
                .getOrNull();
        return new Movie()
                .setId(getIdOrNull(rs))
                .setTitle(getOrNull(rs, Fields.title))
                .setDirector(director);
    }

    private UUID getIdOrNull(ResultSet rs) {
        return Try.of(() -> rs.getString(Fields.id))
                .map(UUID::fromString)
                .getOrNull();
    }

    private String getOrNull(ResultSet rs, String name) {
        return Try.of(() -> rs.getString(name))
                .getOrNull();
    }
}
