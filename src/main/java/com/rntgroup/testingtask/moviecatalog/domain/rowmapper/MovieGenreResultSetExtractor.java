package com.rntgroup.testingtask.moviecatalog.domain.rowmapper;

import com.rntgroup.testingtask.moviecatalog.domain.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieGenreResultSetExtractor extends BaseMovieSubResourcesResultSetExtractor<Genre> {

    private final RowMapper<Genre> genreRowMapper;

    @Override
    protected RowMapper<Genre> getRowMapper() {
        return genreRowMapper;
    }
}
