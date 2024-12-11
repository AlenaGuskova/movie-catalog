package com.rntgroup.testingtask.moviecatalog.domain.rowmapper;

import io.vavr.control.Try;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

abstract class BaseMovieSubResourcesResultSetExtractor<T>
        implements ResultSetExtractor<Map<UUID, List<T>>> {

    /**
     * Extracts mapping movies with movie sub-resources information from the given result set.
     *
     * @param rs the ResultSet to extract data from.
     * @return map mapping movie identifier to movie genres.
     */
    @Override
    public Map<UUID, List<T>> extractData(ResultSet rs)
            throws DataAccessException, SQLException {
        UUID currentMovieId = null;
        var subresources = new LinkedList<T>();
        var movieIdToGenres = new LinkedHashMap<UUID, List<T>>();
        while (rs.next()) {
            if (currentMovieId == null) {
                currentMovieId = getMovieId(rs);
            } else if (!currentMovieId.equals(getMovieId(rs))) {
                movieIdToGenres.put(currentMovieId, subresources);
                currentMovieId = getMovieId(rs);
                subresources = new LinkedList<>();
            }
            subresources.add(getRowMapper().mapRow(rs, rs.getRow()));
        }
        if (currentMovieId != null) {
            movieIdToGenres.put(currentMovieId, subresources);
        }
        return movieIdToGenres;
    }

    protected abstract RowMapper<T> getRowMapper();

    public static UUID getMovieId(ResultSet rs) {
        return UUID.fromString(Try.of(() -> rs.getString("movie_id"))
                .getOrElseTry(() -> rs.getString("movie.id")));
    }
}
