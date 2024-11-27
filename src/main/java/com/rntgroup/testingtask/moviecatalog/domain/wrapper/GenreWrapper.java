package com.rntgroup.testingtask.moviecatalog.domain.wrapper;

import com.rntgroup.testingtask.moviecatalog.domain.model.GenreEntity;
import io.vavr.control.Try;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

import static com.rntgroup.testingtask.moviecatalog.domain.model.GenreEntity.TABLE_NAME;

@Component
public class GenreWrapper implements RowMapper<GenreEntity> {

    private static final String DELIMITER = ".";

    @Override
    public GenreEntity mapRow(ResultSet rs, int i) {
        var id = getOrNull(rs, "id");
        if (id == null) {
            return null;
        }
        var title = getOrNull(rs, "title");
        return new GenreEntity()
                .setId(id)
                .setTitle(title);
    }

    private String getOrNull(ResultSet rs, String name) {
        return Try.of(() -> rs.getString(TABLE_NAME + DELIMITER + name))
                .getOrNull();
    }
}
