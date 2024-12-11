package com.rntgroup.testingtask.moviecatalog.domain.rowmapper;

import com.rntgroup.testingtask.moviecatalog.domain.model.Director;
import com.rntgroup.testingtask.moviecatalog.domain.model.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import static com.rntgroup.testingtask.moviecatalog.domain.rowmapper.BaseMovieSubResourcesResultSetExtractor.getMovieId;

@Component
@RequiredArgsConstructor
public class MovieResultSetExtractor implements ResultSetExtractor<List<Movie>> {

    private final RowMapper<Movie> movieWrapper;
    private final RowMapper<Director> directorWrapper;

    /**
     * Extracts collection of movies with full information from the given result set.
     *
     * @param rs the ResultSet to extract data from.
     * @return collection of movies.
     */
    @Override
    public List<Movie> extractData(ResultSet rs)
            throws DataAccessException, SQLException {
        Movie currentMovie = null;
        var movies = new LinkedList<Movie>();
        while (rs.next()) {
            var movieId = getMovieId(rs);
            if (currentMovie == null) {
                currentMovie = movieWrapper.mapRow(rs, rs.getRow());
            } else if (!currentMovie.getId().equals(movieId)) {
                movies.add(currentMovie);
                currentMovie = movieWrapper.mapRow(rs, rs.getRow());
            }
            var director = directorWrapper.mapRow(rs, rs.getRow());
            if (director.getId() != null) {
                currentMovie.setDirector(director);
            }
        }
        if (currentMovie != null) {
            movies.add(currentMovie);
        }
        return movies;
    }
}
