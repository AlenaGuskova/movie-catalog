package com.rntgroup.testingtask.moviecatalog.domain.dao;

import com.rntgroup.testingtask.moviecatalog.domain.model.MovieEntity;
import com.rntgroup.testingtask.moviecatalog.domain.wrapper.MovieExtractor;
import com.rntgroup.testingtask.moviecatalog.domain.wrapper.MovieWrapper;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static io.vavr.API.$;
import static io.vavr.API.Case;

@Slf4j
@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JdbcMovieDAO implements MovieDAO {

    private static final String SQL_QUERY_FIND_ALL_MOVIES =
            "SELECT m.*, g.*, a.*, d.* FROM movie m " +
            "LEFT JOIN director d ON d.id = m.director_id " +
            "LEFT JOIN movie_genre mg ON mg.movie_id = m.id " +
            "LEFT JOIN genre g ON mg.genre_id = g.id " +
            "LEFT JOIN movie_actor ma ON ma.movie_id = m.id " +
            "LEFT JOIN actor a ON ma.actor_id = a.id";

    private static final String SQL_QUERY_FIND_MOVIES_BY_GENRE =
            "SELECT DISTINCT m.id, m.title FROM movie m " +
            "LEFT JOIN movie_genre mg ON mg.movie_id = m.id " +
            "JOIN genre g ON mg.genre_id = g.id " +
            "WHERE LOWER(g.title) = ?";

    private static final String SQL_QUERY_FIND_MOVIES_BY_NAME =
            "SELECT DISTINCT m.id, m.title FROM movie m " +
            "JOIN director d ON d.id = m.director_id " +
            "LEFT JOIN movie_actor ma ON ma.movie_id = m.id " +
            "JOIN actor a ON ma.actor_id = a.id " +
            "WHERE LOWER(d.first_name) = ? OR LOWER(d.last_name) = ? " +
            "OR LOWER(a.first_name) = ? OR LOWER(a.last_name) = ?";

    private static final String SQL_QUERY_FIND_MOVIES_BY_PREFIX_IN_TITLE =
            "SELECT id, title FROM movie m " +
            "WHERE title ILIKE CONCAT('%', ?,'%')";

    private static final String SQL_QUERY_FIND_FULL_MOVIE_INFORMATION =
            "SELECT m.*, g.*, a.*, d.* FROM movie m " +
            "LEFT JOIN director d ON d.id = m.director_id " +
            "LEFT JOIN movie_genre mg ON mg.movie_id = m.id " +
            "LEFT JOIN genre g ON mg.genre_id = g.id " +
            "LEFT JOIN movie_actor ma ON ma.movie_id = m.id " +
            "LEFT JOIN actor a ON ma.actor_id = a.id " +
            "WHERE m.id = ?";

    private static final String SQL_QUERY_DELETE_MOVIE_BY_ID =
            "DELETE FROM movie m WHERE m.id = ?";

    private static final String SQL_QUERY_DELETE_MOVIE_GENRE_BY_ID =
            "DELETE FROM movie_genre m WHERE m.movie_id = ?";

    private static final String SQL_QUERY_DELETE_MOVIE_ACTOR_BY_ID =
            "DELETE FROM movie_actor m WHERE m.movie_id = ?";

    private static final String SQL_QUERY_INSERT_MOVIE =
            "INSERT INTO movie(id, title, director_id) VALUES(?, ?, ?)";

    private static final String SQL_QUERY_UPDATE_MOVIE_BY_ID =
            "UPDATE movie SET title = ? " +
            "WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final MovieExtractor movieExtractor;
    private final MovieWrapper movieWrapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<MovieEntity> findAll() {
        log.info("Finding all movies from the database");
        return Try.of(() -> jdbcTemplate.query(SQL_QUERY_FIND_ALL_MOVIES, movieExtractor))
                .onSuccess(_ -> log.info("Movies found in the database successfully"))
                .onFailure(e -> log.warn("Error while getting a movie list from the database", e))
                .mapFailure(Case($(), e -> new DAOException("Couldn't find all movies", e)))
                .get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MovieEntity> findByGenre(String genre) {
        var correctGenre = Objects.requireNonNullElse(genre, "")
                .toLowerCase(Locale.ROOT)
                .trim();
        log.info("Finding movies by genre: {} from the database", correctGenre);
        return Try.of(() -> jdbcTemplate.query(SQL_QUERY_FIND_MOVIES_BY_GENRE, movieWrapper, correctGenre))
                .onSuccess(_ -> log.info("Movie found by genre: {} in the database successfully", correctGenre))
                .onFailure(e -> log.warn("Error while getting movies by genre: {} from the database", correctGenre, e))
                .mapFailure(Case($(), e -> new DAOException("Couldn't find movies by genre: " + correctGenre, e)))
                .get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MovieEntity> findByName(String name) {
        var correctName = Objects.requireNonNullElse(name, "")
                .toLowerCase(Locale.ROOT)
                .trim();
        log.info("Finding movies by name of director or actor: {} from the database", correctName);
        return Try.of(() -> jdbcTemplate.query(SQL_QUERY_FIND_MOVIES_BY_NAME,
                        movieWrapper,
                        correctName, correctName, correctName, correctName))
                .onSuccess(_ -> log.info("Movie found by name: {} in the database successfully", correctName))
                .onFailure(e -> log.warn("Error while getting movies by name of director or actor: {} from the database", correctName, e))
                .mapFailure(Case($(), e -> new DAOException("Couldn't find by name of director or actor: " + correctName, e)))
                .get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MovieEntity> findByPrefixInTitle(String prefix) {
        var correctPrefix = Objects.requireNonNullElse(prefix, "")
                .toLowerCase(Locale.ROOT)
                .trim();
        log.info("Finding movies by prefix in title: {} from the database", correctPrefix);
        return Try.of(() -> jdbcTemplate.query(SQL_QUERY_FIND_MOVIES_BY_PREFIX_IN_TITLE,
                        movieWrapper, correctPrefix))
                .onSuccess(_ -> log.info("Movie found by prefix in title: {} in the database successfully", correctPrefix))
                .onFailure(e -> log.warn("Error while getting movies prefix in title: {} from the database", correctPrefix, e))
                .mapFailure(Case($(), e -> new DAOException("Couldn't find by prefix in title: " + correctPrefix, e)))
                .get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MovieEntity findById(String id) {
        log.info("Finding movie information by id: {} from the database", id);
        var results = Try.of(() -> jdbcTemplate.query(SQL_QUERY_FIND_FULL_MOVIE_INFORMATION,
                        movieExtractor, id))
                .onFailure(e -> log.warn("Error while getting movie information by id: {} from the database", id, e))
                .mapFailure(Case($(), e -> new DAOException("Couldn't find movie by id " + id, e)))
                .get();
        if (results.isEmpty()) throw new EntityNotException(id);
        log.info("Movie found by id: {} in the database successfully", id);
        return results.getFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public MovieEntity save(MovieEntity movie) {
        var id = movie.getId();
        log.info("Saving movie with id: {} from the database", id);
        Try.of(() -> jdbcTemplate.update(SQL_QUERY_INSERT_MOVIE, id, movie.getTitle(), movie.getDirector().getId()))
                .onSuccess(_ -> log.info("Movie saved by id: {} into the database successfully", id))
                .onFailure(e -> log.warn("Error while saving movie by id: {} from the database", id, e))
                .mapFailure(Case($(), e -> new DAOException("Couldn't save movie by id " + id, e)))
                .get();
        return findById(id);
    }

    @Override
    @Transactional
    public MovieEntity update(String id, MovieEntity movie) {
        log.info("Updating movie with id: {} from the database", id);
        Try.of(() -> jdbcTemplate.update(SQL_QUERY_UPDATE_MOVIE_BY_ID, movie.getTitle(), id))
                .onSuccess(_ -> log.info("Movie updated by id: {} in the database successfully", id))
                .onFailure(e -> log.warn("Error while saving movie by id: {} from the database", id, e))
                .mapFailure(Case($(), e -> new DAOException("Couldn't update movie by id " + id, e)))
                .get();
        return findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public int delete(String id) {
        log.info("Deleting movie by id: {} from the database", id);
        Try.of(() -> jdbcTemplate.update(SQL_QUERY_DELETE_MOVIE_GENRE_BY_ID, id))
                .onFailure(e -> log.warn("Error while deleting movie genre relationship by id: {} from the database", id, e))
                .mapFailure(Case($(), e -> new DAOException("Couldn't delete movie by id " + id, e)));
        Try.of(() -> jdbcTemplate.update(SQL_QUERY_DELETE_MOVIE_ACTOR_BY_ID, id))
                .onFailure(e -> log.warn("Error while deleting movie actor relationship by id: {} from the database", id, e))
                .mapFailure(Case($(), e -> new DAOException("Couldn't delete movie by id " + id, e)));
        return Try.of(() -> jdbcTemplate.update(SQL_QUERY_DELETE_MOVIE_BY_ID, id))
                .onFailure(e -> log.warn("Error while deleting movie by id: {} from the database", id, e))
                .onSuccess(_ -> log.info("Movie deleted by id: {} in the database successfully", id))
                .mapFailure(Case($(), e -> new DAOException("Couldn't delete movie by id " + id, e)))
                .get();
    }
}
