package com.rntgroup.testingtask.moviecatalog.domain.repository.implementation;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.rntgroup.testingtask.moviecatalog.domain.exception.DataAccessException;
import com.rntgroup.testingtask.moviecatalog.domain.exception.ResourceNotFoundException;
import com.rntgroup.testingtask.moviecatalog.domain.model.Actor;
import com.rntgroup.testingtask.moviecatalog.domain.model.Director;
import com.rntgroup.testingtask.moviecatalog.domain.model.Genre;
import com.rntgroup.testingtask.moviecatalog.domain.model.Movie;
import com.rntgroup.testingtask.moviecatalog.domain.repository.MovieRepository;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

/**
 * {@inheritDoc}
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcMovieRepository implements MovieRepository {

    private final NamedParameterJdbcOperations operations;
    private final ResultSetExtractor<List<Movie>> movieResultSetExtractor;
    private final ResultSetExtractor<Map<UUID, List<Actor>>> movieActorResultSetExtractor;
    private final ResultSetExtractor<Map<UUID, List<Genre>>> movieGenreResultSetExtractor;

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Movie> findAll() {
        log.debug("Finding all movies from the database");
        var query = """
                SELECT movie.id as id, movie.title, director.id, director.first_name, director.last_name
                FROM movie
                LEFT JOIN director ON director.id = movie.director_id""";
        var movies = Try.of(() -> operations.query(query, movieResultSetExtractor))
                .onSuccess(_ -> log.info("Movies found in the database successfully"))
                .onFailure(e -> log.warn("Exception occurred finding all movies from the database", e))
                .getOrElseThrow(_ -> new DataAccessException("Exception occurred finding all movies from the database"));
        return enrichMovies(movies);
    }

    private List<Movie> enrichMovies(List<Movie> movies) {
        var moviesIds = movies.stream()
                .map(Movie::getId)
                .collect(Collectors.toSet());
        log.debug("Enriching movies with ids:'{}' with data from the database", moviesIds);
        var foundMovieIdToActors = getActors(moviesIds);
        var foundMovieIdToGenres = getGenres(moviesIds);
        var result = movies.stream()
                .map(movie -> movie.setActors(foundMovieIdToActors.get(movie.getId())))
                .map(movie -> movie.setGenres(foundMovieIdToGenres.get(movie.getId())))
                .toList();
        log.debug("Movies with ids:'{}' were enriched with data from the database successfully", moviesIds);
        return result;
    }

    private Map<UUID, List<Actor>> getActors(Set<UUID> ids) {
        var query = """
                SELECT id, first_name, last_name, movie_id
                FROM actor a
                JOIN movie_actor ma ON ma.actor_id = a.id
                WHERE ma.movie_id IN (:ids)
                """;
        var parameters = Map.of("ids", ids);
        return Try.of(() -> operations.query(query, parameters, movieActorResultSetExtractor))
                .onSuccess(_ -> log.debug("Movie actors found successfully"))
                .onFailure(e -> log.warn("Exception occurred when finding movies actors by ids:'{}' from the database", ids, e))
                .getOrElse(new HashMap<>());
    }

    private Map<UUID, List<Genre>> getGenres(Set<UUID> ids) {
        var query = """
                SELECT id, title, movie_id
                FROM genre
                JOIN movie_genre ON movie_genre.genre_id = genre.id
                WHERE movie_genre.movie_id IN (:ids)
                """;
        var parameters = Map.of("ids", ids);
        return Try.of(() -> operations.query(query, parameters, movieGenreResultSetExtractor))
                .onSuccess(_ -> log.debug("Movie genres found successfully"))
                .onFailure(e -> log.warn("Exception occurred finding movies genres by ids:'{}' from the database", ids, e))
                .getOrElse(new HashMap<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> findByGenreIgnoreCase(String genre) {
        log.debug("Finding movies by genre:'{}' from the database", genre);
        var query = """
                SELECT movie.id as id, movie.title, director.id, director.first_name, director.last_name
                FROM movie
                JOIN movie_genre ON movie_genre.movie_id = movie.id
                JOIN genre g ON movie_genre.genre_id = g.id
                LEFT JOIN director ON director.id = movie.director_id
                WHERE LOWER(g.title) = :genre""";
        var parameters = Map.of("genre", genre);
        var movies = Try.of(() -> operations.query(query, parameters, movieResultSetExtractor))
                .onSuccess(_ -> log.info("Movies found by genre:'{}' in the database successfully", genre))
                .onFailure(e -> log.warn("Exception occurred when getting movies by genre:'{}' from the database", genre, e))
                .getOrElseThrow(_ -> new DataAccessException("getting movies by genre: " + genre));
        return enrichMovies(movies);
    }

    /**
     * {@inheritDoc}
     */
    public List<Movie> findByNameIgnoreCase(String name) {
        log.debug("Finding movies by name of director or actor:'{}' from the database", name);
        var query = """
                SELECT movie.id as id, movie.title, director.id, director.first_name, director.last_name
                FROM movie
                LEFT JOIN director ON director.id = movie.director_id
                JOIN movie_actor ma ON ma.movie_id = movie.id
                JOIN actor a ON ma.actor_id = a.id
                WHERE LOWER(director.first_name) = :director_first_name OR LOWER(director.last_name) = :director_last_name
                OR LOWER(a.first_name) = :actor_first_name OR LOWER(a.last_name) = :actor_last_name""";
        var parameters = Map.of(
                "director_first_name", name,
                "director_last_name", name,
                "actor_first_name", name,
                "actor_last_name", name
        );
        var movies = Try.of(() -> operations.query(query, parameters, movieResultSetExtractor))
                .onSuccess(_ -> log.info("Movies found by name:'{}' in the database successfully", name))
                .onFailure(e -> log.warn("Exception occurred getting movies by name of director or actor:'{}' from the database", name, e))
                .getOrElseThrow(_ -> new DataAccessException("getting movie by name of director or actor:" + name));
        return enrichMovies(movies);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> findByPrefixInTitleIgnoreCase(String prefix) {
        log.debug("Finding movies by prefix in title:'{}' from the database", prefix);
        var query = """
                SELECT movie.id as id, movie.title, director.id, director.first_name, director.last_name
                FROM movie
                LEFT JOIN director ON director.id = movie.director_id
                WHERE movie.title ILIKE CONCAT('%', :prefix,'%')""";
        var parameters = Map.of("prefix", prefix);
        var movies = Try.of(() -> operations.query(query, parameters, movieResultSetExtractor))
                .onSuccess(_ -> log.info("Movies found by prefix in title:'{}' in the database successfully", prefix))
                .onFailure(e -> log.warn("Exception occurred when getting movies prefix in title:'{}' from the database", prefix, e))
                .getOrElseThrow(_ -> new DataAccessException("getting movies prefix in title:" + prefix));
        return enrichMovies(movies);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie findById(String id) {
        log.debug("Finding movie by id:'{}' from the database", id);
        var query = """
                SELECT movie.id as id, movie.title, director.id, director.first_name, director.last_name
                FROM movie
                LEFT JOIN director ON director.id = movie.director_id
                WHERE movie.id = :id""";
        var parameters = Map.of("id", id);
        var movies = Try.of(() -> operations.query(query, parameters, movieResultSetExtractor))
                .onSuccess(_ -> log.info("Movie found by id:'{}' in the database successfully", id))
                .onFailure(e -> log.warn("Exception occurred when getting movie by id:'{}' from the database", id, e))
                .getOrElseThrow(_ -> new DataAccessException("getting movie by id:" + id));
        return Try.of(() -> enrichMovies(movies))
                .mapTry(List::getFirst)
                .onSuccess(_ -> log.debug("Movie found by id:'{}' in the database successfully", id))
                .onFailure(e -> log.warn("Movie not found by id:'{}' from the database", id, e))
                .getOrElseThrow(() -> new ResourceNotFoundException(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie save(Movie movie) {
        var id = movie.getId().toString();
        log.debug("Saving movie with id:'{}' from the database", id);
        var query = "INSERT INTO movie(id, title, director_id) "
                    + "VALUES(:id, :title, :director_id)";
        var parameters = new HashMap<String, Object>();
        parameters.put("id", id);
        parameters.put("title", movie.getTitle());
        var directorId = Optional.ofNullable(movie.getDirector())
                .map(Director::getId)
                .orElse(null);
        parameters.put("director_id", directorId);
        Try.of(() -> operations.update(query, parameters))
                .onSuccess(_ -> log.info("Movie saved by id:'{}' into the database successfully", id))
                .onFailure(e -> log.warn("Exception occurred when saving movie by id:'{}' from the database", id, e))
                .getOrElseThrow(_ -> new DataAccessException("saving movie by id:" + id));
        return findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie update(Movie movie) {
        var id = movie.getId().toString();
        log.debug("Updating movie with id:'{}' from the database", id);
        var query = movie.getDirector() == null
                ? "UPDATE movie SET title = :title WHERE id = :id"
                : "UPDATE movie SET title = :title, director_id = :director_id WHERE id = :id";
        var parameters = new HashMap<String, Object>();
        parameters.put("id", id);
        parameters.put("title", movie.getTitle());
        parameters.put("director_id", Optional.ofNullable(movie.getDirector()).map(Director::getId).orElse(null));
        var numberOfRowsUpdated = Try.of(() -> operations.update(query, parameters))
                .onSuccess(_ -> log.info("Movie updated by id:'{}' in the database successfully", id))
                .onFailure(e -> log.warn("Exception occurred when updating movie by id:'{}' from the database", id, e))
                .getOrElseThrow(_ -> new DataAccessException("updating movie by id:" + id));
        if (numberOfRowsUpdated == 0) {
            throw new ResourceNotFoundException(id);
        }
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
                .onFailure(e -> log.warn("Exception occurred when deleting movie genre relationship by id:'{}' from the database", id, e))
                .getOrElseThrow(_ -> new DataAccessException("deleting movie genres by movie id:" + id));

        var sqlQueryDeleteMovieActorById = "DELETE FROM movie_actor WHERE movie_id = :movie_id";
        Try.of(() -> operations.update(sqlQueryDeleteMovieActorById, parameters))
                .onSuccess(_ -> log.info("Movie actors deleted by movie id:'{}' in the database successfully", id))
                .onFailure(e -> log.warn("Exception occurred when deleting movie actor relationship by id:'{}' from the database", id, e))
                .getOrElseThrow(_ -> new DataAccessException("deleting movie actors by movie id:" + id));

        var sqlQueryDeleteMovieById = "DELETE FROM movie WHERE id = :movie_id";
        var numberOfRowsDeleted = Try.of(() -> operations.update(sqlQueryDeleteMovieById, parameters))
                .onFailure(e -> log.warn("Exception occurred when deleting movie by id:'{}' from the database", id, e))
                .onSuccess(_ -> log.info("Movie deleted by id:'{}' in the database successfully", id))
                .getOrElseThrow(_ -> new DataAccessException("deleting movie by movie id:" + id));
        if (numberOfRowsDeleted == 0) {
            throw new ResourceNotFoundException(id);
        }
        return numberOfRowsDeleted;
    }
}
