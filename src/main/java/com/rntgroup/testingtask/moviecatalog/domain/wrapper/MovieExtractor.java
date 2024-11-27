package com.rntgroup.testingtask.moviecatalog.domain.wrapper;

import com.rntgroup.testingtask.moviecatalog.domain.model.ActorEntity;
import com.rntgroup.testingtask.moviecatalog.domain.model.GenreEntity;
import com.rntgroup.testingtask.moviecatalog.domain.model.MovieEntity;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MovieExtractor implements ResultSetExtractor<List<MovieEntity>> {

    private static final String DELIMITER = ".";
    private final MovieWrapper movieWrapper;
    private final GenreWrapper genreWrapper;
    private final ActorWrapper actorWrapper;
    private final DirectorWrapper directorWrapper;

    /**
     * Extracts collection of movies with full information from the given result set.
     *
     * @param rs the ResultSet to extract data from.
     * @return collection of movies.
     */
    @Override
    public List<MovieEntity> extractData(ResultSet rs)
            throws DataAccessException, SQLException {
        MovieEntity currentMovie = null;
        var movies = new ArrayList<MovieEntity>();
        var genres = new LinkedHashMap<String, GenreEntity>();
        var actors = new LinkedHashMap<String, ActorEntity>();

        while (rs.next()) {
            var movieId = getId(rs, MovieEntity.TABLE_NAME);
            if (currentMovie == null) {
                currentMovie = movieWrapper.mapRow(rs, rs.getRow());
            } else if (!currentMovie.getId().equals(movieId)) {
                currentMovie.setGenres(genres.values().stream().toList());
                currentMovie.setActors(actors.values().stream().toList());

                movies.add(currentMovie);

                currentMovie = movieWrapper.mapRow(rs, rs.getRow());
                genres = new LinkedHashMap<>();
                actors = new LinkedHashMap<>();
            }
            currentMovie.setDirector(directorWrapper.mapRow(rs, rs.getRow()));

            var genreId = getId(rs, GenreEntity.TABLE_NAME);
            if (genreId != null) {
                genres.putIfAbsent(genreId, genreWrapper.mapRow(rs, rs.getRow()));
            }

            var actorId = getId(rs, ActorEntity.TABLE_NAME);
            if (actorId != null) {
                actors.putIfAbsent(actorId, actorWrapper.mapRow(rs, rs.getRow()));
            }
        }
        if (currentMovie != null) {
            currentMovie.setGenres(genres.values().stream().toList());
            currentMovie.setActors(actors.values().stream().toList());

            movies.add(currentMovie);
        }
        return movies;
    }

    private String getId(ResultSet rs, String tableName) {
        return Try.of(() -> rs.getString(tableName + DELIMITER + "id"))
                .getOrNull();
    }
}
