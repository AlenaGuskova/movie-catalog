package com.rntgroup.testingtask.moviecatalog.domain.wrapper;

import com.rntgroup.testingtask.moviecatalog.domain.model.MovieEntity;
import io.vavr.control.Try;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

import static com.rntgroup.testingtask.moviecatalog.domain.model.MovieEntity.TABLE_NAME;

@Component
public class MovieWrapper implements RowMapper<MovieEntity> {

    private static final String DELIMITER = ".";

    @Override
    public MovieEntity mapRow(ResultSet rs, int i) {
        var id = getOrNull(rs, "id");
        var title = getOrNull(rs, "title");
        return new MovieEntity()
                .setId(id)
                .setTitle(title);
    }

    private String getOrNull(ResultSet rs, String name) {
        return Try.of(() -> rs.getString(TABLE_NAME + DELIMITER + name))
                .getOrNull();
    }
}
