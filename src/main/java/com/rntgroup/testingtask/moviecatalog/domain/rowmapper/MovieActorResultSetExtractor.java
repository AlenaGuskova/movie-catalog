package com.rntgroup.testingtask.moviecatalog.domain.rowmapper;

import com.rntgroup.testingtask.moviecatalog.domain.model.Actor;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieActorResultSetExtractor extends BaseMovieSubResourcesResultSetExtractor<Actor> {

    private final RowMapper<Actor> actorRowMapper;

    @Override
    protected RowMapper<Actor> getRowMapper() {
        return actorRowMapper;
    }
}
