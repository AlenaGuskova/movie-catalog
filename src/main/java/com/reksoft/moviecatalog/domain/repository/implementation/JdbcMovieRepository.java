package com.reksoft.moviecatalog.domain.repository.implementation;

import com.reksoft.moviecatalog.domain.repository.GenreRepository;
import com.reksoft.moviecatalog.domain.repository.MovieActorRepository;
import com.reksoft.moviecatalog.domain.repository.MovieGenreRepository;
import com.reksoft.moviecatalog.domain.repository.MovieRepository;
import com.reksoft.moviecatalog.domain.exception.ResourceNotFoundException;
import com.reksoft.moviecatalog.domain.model.Actor;
import com.reksoft.moviecatalog.domain.model.Director;
import com.reksoft.moviecatalog.domain.model.Genre;
import com.reksoft.moviecatalog.domain.model.Movie;
import com.reksoft.moviecatalog.domain.model.MovieActor;
import com.reksoft.moviecatalog.domain.model.MovieGenre;
import com.reksoft.moviecatalog.domain.repository.ActorRepository;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * {@inheritDoc}
 */
@Repository
@RequiredArgsConstructor
public class JdbcMovieRepository implements MovieRepository {

    private final NamedParameterJdbcOperations operations;
    private final RowMapper<Movie> movieRowMapper;
    private final ActorRepository actorRepository;
    private final GenreRepository genreRepository;
    private final MovieActorRepository movieActorRepository;
    private final MovieGenreRepository movieGenreRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Movie> findAll() {
        var query = """
            SELECT movie.id as id, movie.title, director.id, director.first_name, director.last_name
            FROM movie
            LEFT JOIN director ON director.id = movie.director_id""";
        return enrichMovies(operations.query(query, movieRowMapper));
    }

    private List<Movie> enrichMovies(List<Movie> movies) {
        var foundMovieIdToActors = getMovieIdToActors();
        var foundMovieIdToGenres = getMovieIdToGenres();
        return movies.stream()
                   .map(movie -> movie.setActors(foundMovieIdToActors.get(movie.getId())))
                   .map(movie -> movie.setGenres(foundMovieIdToGenres.get(movie.getId())))
                   .toList();
    }

    private Map<UUID, List<Actor>> getMovieIdToActors() {
        var actorIdToActor = actorRepository.findAll()
                                 .stream()
                                 .collect(toMap(Actor::getId, Function.identity()));
        return movieActorRepository.getMovieActors()
                   .stream()
                   .collect(groupingBy(MovieActor::getMovieId,
                       mapping((MovieActor movieActor) ->
                                   actorIdToActor.get(movieActor.getActorId()), toList())));
    }

    private Map<UUID, List<Genre>> getMovieIdToGenres() {
        var genreIdToGenre = genreRepository.findAll()
                                 .stream()
                                 .collect(toMap(Genre::getId, Function.identity()));
        return movieGenreRepository.getMovieGenres()
                   .stream()
                   .collect(groupingBy(MovieGenre::getMovieId,
                       mapping((MovieGenre movieActor) ->
                                   genreIdToGenre.get(movieActor.getGenreId()), toList())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> findByGenreIgnoreCase(String genre) {
        var query = """
            SELECT movie.id as id, movie.title, director.id, director.first_name, director.last_name
            FROM movie
            JOIN movie_genre ON movie_genre.movie_id = movie.id
            JOIN genre g ON movie_genre.genre_id = g.id
            LEFT JOIN director ON director.id = movie.director_id
            WHERE LOWER(g.title) = :genre""";
        var parameters = Map.of("genre", genre);
        return enrichMovies(operations.query(query, parameters, movieRowMapper));
    }

    /**
     * {@inheritDoc}
     */
    public List<Movie> findByNameIgnoreCase(String name) {
        var query = """
            SELECT DISTINCT movie.id as id, movie.title, director.id, director.first_name, director.last_name
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
        return enrichMovies(operations.query(query, parameters, movieRowMapper));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> findByPrefixInTitleIgnoreCase(String prefix) {
        var query = """
            SELECT movie.id as id, movie.title, director.id, director.first_name, director.last_name
            FROM movie
            LEFT JOIN director ON director.id = movie.director_id
            WHERE movie.title ILIKE CONCAT('%', :prefix,'%')""";
        var parameters = Map.of("prefix", prefix);
        return enrichMovies(operations.query(query, parameters, movieRowMapper));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie findById(String id) {
        var query = """
            SELECT movie.id as id, movie.title, director.id, director.first_name, director.last_name
            FROM movie
            LEFT JOIN director ON director.id = movie.director_id
            WHERE movie.id = :id""";
        var parameters = Map.of("id", id);
        return Try.of(() -> operations.queryForObject(query, parameters, movieRowMapper))
                   .map(this::enrichMovie)
                   .getOrElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Override
    public boolean existById(String id) {
        var query = """
            SELECT movie.id as id FROM movie
            WHERE movie.id = :id""";
        var parameters = Map.of("id", id);
        return !operations.query(query, parameters, movieRowMapper).isEmpty();
    }

    private Movie enrichMovie(Movie movie) {
        var movieId = movie.getId();
        return new Movie()
                   .setId(movieId)
                   .setTitle(movie.getTitle())
                   .setDirector(movie.getDirector())
                   .setActors(actorRepository.findByMovieId(movieId))
                   .setGenres(genreRepository.findByMovieId(movieId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie save(Movie movie) {
        var id = movie.getId().toString();
        var query = "INSERT INTO movie(id, title, director_id) "
                        + "VALUES(:id, :title, :director_id)";
        var parameters = new HashMap<String, Object>();
        parameters.put("id", id);
        parameters.put("title", movie.getTitle());
        parameters.put("director_id", Optional.ofNullable(movie.getDirector()).map(Director::getId).orElse(null));
        operations.update(query, parameters);
        return findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie update(Movie movie) {
        var id = movie.getId().toString();
        if (existById(id)) {
            var parameters = new HashMap<String, Object>();
            parameters.put("id", id);
            parameters.put("title", movie.getTitle());
            parameters.put("director_id", Optional.ofNullable(movie.getDirector()).map(Director::getId).orElse(null));
            var query = "UPDATE movie SET title = :title, director_id = :director_id WHERE id = :id";
            operations.update(query, parameters);
            return findById(id);
        } else {
            throw new ResourceNotFoundException(id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int delete(String id) {
        if (existById(id)) {
            movieGenreRepository.deleteMovieGenres(id);
            movieActorRepository.deleteMovieActors(id);
            var query = "DELETE FROM movie WHERE id = :id";
            return operations.update(query, Map.of("id", id));
        } else {
            throw new ResourceNotFoundException(id);
        }
    }
}
