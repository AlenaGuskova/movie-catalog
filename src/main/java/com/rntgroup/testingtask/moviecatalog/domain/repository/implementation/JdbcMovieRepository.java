package com.rntgroup.testingtask.moviecatalog.domain.repository.implementation;

import com.rntgroup.testingtask.moviecatalog.domain.exception.DataAccessException;
import com.rntgroup.testingtask.moviecatalog.domain.exception.ResourceNotFoundException;
import com.rntgroup.testingtask.moviecatalog.domain.model.Actor;
import com.rntgroup.testingtask.moviecatalog.domain.model.Director;
import com.rntgroup.testingtask.moviecatalog.domain.model.Genre;
import com.rntgroup.testingtask.moviecatalog.domain.model.Movie;
import com.rntgroup.testingtask.moviecatalog.domain.repository.MovieRepository;
import com.rntgroup.testingtask.moviecatalog.domain.rowwrapper.ActorRowWrapper;
import com.rntgroup.testingtask.moviecatalog.domain.rowwrapper.DirectorRowWrapper;
import com.rntgroup.testingtask.moviecatalog.domain.rowwrapper.GenreRowWrapper;
import com.rntgroup.testingtask.moviecatalog.domain.rowwrapper.MovieRowWrapper;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * {@inheritDoc}
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcMovieRepository implements MovieRepository {

    private final NamedParameterJdbcOperations operations;
    private final MovieRowWrapper movieRowWrapper;
    private final GenreRowWrapper genreRowWrapper;
    private final ActorRowWrapper actorRowWrapper;
    private final DirectorRowWrapper directorRowWrapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Movie> findAll() {
        log.debug("Finding all movies from the database");
        var query = "SELECT id, title FROM movie";
        return Try.of(() -> operations.query(query, movieRowWrapper))
                .onSuccess(_ -> log.info("Movies found in the database successfully"))
                .onFailure(_ -> log.warn("Error while getting a movie list from the database"))
                .getOrElseThrow(e -> new DataAccessException("Couldn't find all movies", e));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> findByGenreIgnoreCase(String genre) {
        log.debug("Finding movies by genre:'{}' from the database", genre);
        var query = """
                SELECT m.id, m.title
                FROM movie m
                JOIN movie_genre mg ON mg.movie_id = m.id
                JOIN genre g ON mg.genre_id = g.id
                WHERE LOWER(g.title) = :genre""";
        var parameters = Map.of("genre", genre);
        return Try.of(() -> operations.query(query, parameters, movieRowWrapper))
                .onSuccess(_ -> log.info("Movie found by genre:'{}' in the database successfully", genre))
                .onFailure(_ -> log.warn("Error while getting movies by genre:'{}' from the database", genre))
                .getOrElseThrow(e -> new DataAccessException("Couldn't find movies by genre:" + genre, e));
    }

    /**
     * {@inheritDoc}
     */
    public List<Movie> findByNameIgnoreCase(String name) {
        log.debug("Finding movies by name of director or actor:'{}' from the database", name);
        var query = """
                SELECT DISTINCT m.id, m.title FROM movie m
                JOIN director d ON d.id = m.director_id
                JOIN movie_actor ma ON ma.movie_id = m.id
                JOIN actor a ON ma.actor_id = a.id
                WHERE LOWER(d.first_name) = :director_first_name OR LOWER(d.last_name) = :director_last_name
                OR LOWER(a.first_name) = :actor_first_name OR LOWER(a.last_name) = :actor_last_name""";
        var parameters = Map.of(
                "director_first_name", name,
                "director_last_name", name,
                "actor_first_name", name,
                "actor_last_name", name
        );
        return Try.of(() -> operations.query(query, parameters, movieRowWrapper))
                .onSuccess(_ -> log.info("Movie found by name:'{}' in the database successfully", name))
                .onFailure(_ -> log.warn("Error while getting movies by name of director or actor:'{}' from the database", name))
                .getOrElseThrow(e -> new DataAccessException("Couldn't find by name of director or actor:" + name, e));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> findByPrefixInTitleIgnoreCase(String prefix) {
        log.debug("Finding movies by prefix in title:'{}' from the database", prefix);
        var query = "SELECT id, title FROM movie WHERE title ILIKE CONCAT('%', :prefix,'%')";
        var parameters = Map.of("prefix", prefix);
        return Try.of(() -> operations.query(query, parameters, movieRowWrapper))
                .onSuccess(_ -> log.info("Movie found by prefix in title:'{}' in the database successfully", prefix))
                .onFailure(_ -> log.warn("Error while getting movies prefix in title:'{}' from the database", prefix))
                .getOrElseThrow(e -> new DataAccessException("Couldn't find by prefix in title:" + prefix, e));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie findById(String id) {
        log.debug("Finding movie by id:'{}' from the database", id);
        var query = "SELECT id, title FROM movie WHERE id = :id";
        var parameters = Map.of("id", id);
        var movie = Try.of(() -> operations.query(query, parameters, movieRowWrapper))
                .mapTry(List::getFirst)
                .onFailure(_ -> log.warn("Movie not found by id:'{}' from the database", id))
                .getOrElseThrow(() -> new ResourceNotFoundException(id));
        return Try.of(() -> movie.setDirector(getDirector(id)))
                .onFailure(e -> log.warn("Exception occurred when finding movie director by id:'{}' from the database", id, e))
                .mapTry(m -> m.setActors(getActors(id)))
                .onFailure(e -> log.warn("Exception occurred when finding movie actors by id:'{}' from the database", id, e))
                .mapTry(m -> m.setGenres(getGenres(id)))
                .onFailure(_ -> log.warn("Exception occurred when finding movie genres by id:'{}' from the database", id))
                .onSuccess(_ -> log.debug("Movie found by id:'{}' in the database successfully", id))
                .getOrElseThrow(e -> new DataAccessException("Exception occurred when in database finding movie by id:" + id, e));
    }

    private List<Genre> getGenres(String id) {
        var query = """
                SELECT g.id, g.title
                FROM genre g
                JOIN movie_genre mg ON mg.genre_id = g.id
                WHERE mg.movie_id = :id
                """;
        var parameters = Map.of("id", id);
        return Try.of(() -> operations.query(query, parameters, genreRowWrapper))
                .filter(genres -> !genres.isEmpty())
                .getOrNull();
    }

    private List<Actor> getActors(String id) {
        var query = """
                SELECT a.id, a.first_name, a.last_name
                FROM actor a
                JOIN movie_actor ma ON ma.actor_id = a.id
                WHERE ma.movie_id = :id
                """;
        var parameters = Map.of("id", id);
        return Try.of(() -> operations.query(query, parameters, actorRowWrapper))
                .filter(genres -> !genres.isEmpty())
                .getOrNull();
    }

    private Director getDirector(String id) {
        var query = """
                SELECT d.id, d.first_name, d.last_name
                FROM director d
                JOIN movie m ON d.id = m.director_id
                WHERE m.id = :id
                """;
        var parameters = Map.of("id", id);
        return Try.of(() -> operations.queryForObject(query, parameters, directorRowWrapper))
                .getOrNull();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie save(Movie movie) {
        var id = movie.getId();
        log.debug("Saving movie with id:'{}' from the database", id);
        var query = "INSERT INTO movie(id, title, director_id) " +
                "VALUES(:id, :title, :director_id)";
        var parameters = new HashMap<String, String>();
        parameters.put("id", id);
        parameters.put("title", movie.getTitle());
        parameters.put("director_id", Optional.ofNullable(movie.getDirector()).map(Director::getId).orElse(null));
        Try.of(() -> operations.update(query, parameters))
                .onSuccess(_ -> log.info("Movie saved by id:'{}' into the database successfully", id))
                .onFailure(_ -> log.warn("Error while saving movie by id:'{}' from the database", id))
                .getOrElseThrow(e -> new DataAccessException("Couldn't save movie by id " + id, e));
        return findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie update(Movie movie) {
        var id = movie.getId();
        log.debug("Updating movie with id:'{}' from the database", id);
        var query = movie.getDirector() == null
                ? "UPDATE movie SET title = :title WHERE id = :id"
                : "UPDATE movie SET title = :title, director_id = :director_id WHERE id = :id";
        var parameters = new HashMap<String, String>();
        parameters.put("id", id);
        parameters.put("title", movie.getTitle());
        parameters.put("director_id", Optional.ofNullable(movie.getDirector()).map(Director::getId).orElse(null));
        Try.of(() -> operations.update(query, parameters))
                .onSuccess(_ -> log.info("Movie updated by id:'{}' in the database successfully", id))
                .onFailure(_ -> log.warn("Error while updating movie by id:'{}' from the database", id))
                .getOrElseThrow(e -> new DataAccessException("Couldn't update movie by id " + id, e));
        return findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int delete(String id) {
        log.debug("Deleting movie by id:'{}' from the database", id);
        var parameters = Map.of("movie_id", id);
        var sqlQueryDeleteMovieGenreById = "DELETE FROM movie_genre WHERE movie_id = :movie_id";
        Try.of(() -> operations.update(sqlQueryDeleteMovieGenreById, parameters))
                .onSuccess(_ -> log.info("Movie genres deleted by movie id:'{}' in the database successfully", id))
                .onFailure(_ -> log.warn("Error while deleting movie genre relationship by id:'{}' from the database", id))
                .getOrElseThrow(e -> new DataAccessException("Couldn't delete movie by id " + id, e));

        var sqlQueryDeleteMovieActorById = "DELETE FROM movie_actor WHERE movie_id = :movie_id";
        Try.of(() -> operations.update(sqlQueryDeleteMovieActorById, parameters))
                .onSuccess(_ -> log.info("Movie actors deleted by movie id:'{}' in the database successfully", id))
                .onFailure(_ -> log.warn("Error while deleting movie actor relationship by id:'{}' from the database", id))
                .getOrElseThrow(e -> new DataAccessException("Couldn't delete movie by id " + id, e));

        var sqlQueryDeleteMovieById = "DELETE FROM movie WHERE id = :movie_id";
        return Try.of(() -> operations.update(sqlQueryDeleteMovieById, parameters))
                .onFailure(_ -> log.warn("Error while deleting movie by id:'{}' from the database", id))
                .onSuccess(_ -> log.info("Movie deleted by id:'{}' in the database successfully", id))
                .getOrElseThrow(e -> new DataAccessException("Couldn't delete movie by id " + id, e));
    }
}
