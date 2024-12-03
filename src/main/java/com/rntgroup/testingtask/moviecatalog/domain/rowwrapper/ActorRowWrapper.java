package com.rntgroup.testingtask.moviecatalog.domain.rowwrapper;

import com.rntgroup.testingtask.moviecatalog.domain.model.Actor;
import io.vavr.control.Try;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

/**
 * {@inheritDoc}
 */
@Component
public class ActorRowWrapper implements RowMapper<Actor> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Actor mapRow(ResultSet rs, int i) {
        var id = getOrNull(rs, "id");
        var firstName = getOrNull(rs, "first_name");
        var lastName = getOrNull(rs, "last_name");
        return new Actor()
                .setId(id)
                .setFirstName(firstName)
                .setLastName(lastName);
    }

    private String getOrNull(ResultSet rs, String name) {
        return Try.of(() -> rs.getString(name))
                .getOrNull();
    }
}
