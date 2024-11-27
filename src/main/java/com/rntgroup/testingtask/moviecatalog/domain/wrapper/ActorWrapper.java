package com.rntgroup.testingtask.moviecatalog.domain.wrapper;

import com.rntgroup.testingtask.moviecatalog.domain.model.ActorEntity;
import io.vavr.control.Try;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

import static com.rntgroup.testingtask.moviecatalog.domain.model.ActorEntity.TABLE_NAME;

@Component
public class ActorWrapper implements RowMapper<ActorEntity> {

    private static final String DELIMITER = ".";

    @Override
    public ActorEntity mapRow(ResultSet rs, int i) {
        var id = getOrNull(rs, "id");
        if (id == null) {
            return null;
        }
        var firstName = getOrNull(rs, "first_name"); // FIXME
        var lastName = getOrNull(rs, "last_name");
        return new ActorEntity()
                .setId(id)
                .setFirstName(firstName)
                .setLastName(lastName);
    }

    private String getOrNull(ResultSet rs, String name) {
        return Try.of(() -> rs.getString(TABLE_NAME + DELIMITER + name))
                .getOrNull();
    }
}
