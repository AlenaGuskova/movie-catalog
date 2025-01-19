package com.reksoft.moviecatalog.service.implementation;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import com.reksoft.moviecatalog.api.request.CreateMovieRequest;
import com.reksoft.moviecatalog.api.request.CreateMovieRequest.MovieRequest;
import com.reksoft.moviecatalog.api.request.UpdateMovieRequest;
import com.reksoft.moviecatalog.api.response.dto.MovieDto;
import com.reksoft.moviecatalog.common.audit.annotation.AuditEvent;
import com.reksoft.moviecatalog.service.MovieService;
import com.reksoft.moviecatalog.service.exception.NullAndBlankValidationException;
import com.reksoft.moviecatalog.service.utils.IdHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 * This is the service decorator for checking the input data passed to the movie service.
 */
@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class InputValidationMovieService implements MovieService {

    private final MovieService origin;
    private final IdHandler idHandler;

    /**
     * {@inheritDoc}
     */
    @Override
    @AuditEvent(name = "GET_ALL_MOVIES")
    public Collection<MovieDto> getMovies() {
        return origin.getMovies();
    }

    /**
     * {@inheritDoc}
     * It checks the genre for null and blank and then transfer
     * the responsibility of getting movies by genre to another {@link MovieService}.
     */
    @Override
    @AuditEvent(name = "GET_MOVIE_BY_GENRE")
    public Collection<MovieDto> getByGenre(String genre) {
        checkForNullAndBlank(genre, "genre");
        var correctGenre = genre.toLowerCase(Locale.ROOT).trim();
        return origin.getByGenre(correctGenre);
    }

    /**
     * {@inheritDoc}
     * It checks the name for null and blank and then transfer
     * the responsibility of getting movies by name to another {@link MovieService}.
     */
    @Override
    @AuditEvent(name = "GET_MOVIE_BY_HUMAN_NAME")
    public Collection<MovieDto> getByName(String name) {
        checkForNullAndBlank(name, "name");
        var correctName = name.toLowerCase(Locale.ROOT).trim();
        return origin.getByName(correctName);
    }

    /**
     * {@inheritDoc}
     * It checks the prefix for null and blank and then transfer
     * the responsibility of getting movies by prefix to another {@link MovieService}.
     */
    @Override
    @AuditEvent(name = "GET_MOVIE_BY_PREFIX_IN_TITLE")
    public Collection<MovieDto> getByPrefixInTitle(String prefix) {
        checkForNullAndBlank(prefix, "prefix");
        var correctPrefix = prefix.toLowerCase(Locale.ROOT).trim();
        return origin.getByPrefixInTitle(correctPrefix);
    }

    private void checkForNullAndBlank(String fieldValue, String fieldName) {
        if (Objects.isNull(fieldValue) || fieldValue.isBlank()) {
            log.warn("Invalid field from movie request: '{}'", fieldName);
            throw new NullAndBlankValidationException(fieldName, fieldValue);
        }
    }

    /**
     * {@inheritDoc}
     * It validates the id and then transfer the responsibility
     * of getting movies by id to another {@link MovieService}.
     */
    @Override
    @AuditEvent(name = "GET_MOVIE_BY_ID")
    public MovieDto getById(String id) {
        checkForNullAndBlank(id, "id");
        idHandler.isValid(id);
        return origin.getById(id);
    }

    /**
     * {@inheritDoc}
     * It validates the id and then transfer the responsibility
     * of deleting movies by id to another {@link MovieService}.
     */
    @Override
    @AuditEvent(name = "DELETE_MOVIE_BY_ID")
    public int delete(String id) {
        checkForNullAndBlank(id, "id");
        idHandler.isValid(id);
        return origin.delete(id);
    }

    /**
     * {@inheritDoc}
     * It validates the identifiers of all movie sub-resources and generates a new movie identifier.
     * Then transfer the responsibility of creating movies to another {@link MovieService}.
     */
    @Override
    @AuditEvent(name = "CREATE_MOVIE")
    public MovieDto create(CreateMovieRequest request) {
        var movie = request.movie();
        Optional.of(movie)
                .map(CreateMovieRequest.MovieRequest::directorId)
                .ifPresent(idHandler::isValid);
        var requestToCreateMovie = enrichWithId(movie);
        return origin.create(requestToCreateMovie);
    }

    private CreateMovieRequest enrichWithId(MovieRequest movie) {
        return new CreateMovieRequest(
                new MovieRequest(
                        idHandler.generateId(),
                        movie.title(),
                        movie.directorId())
        );
    }

    /**
     * {@inheritDoc}
     * It validates the identifiers of all movie sub-resources and then transfer
     * the responsibility of updating movies to another {@link MovieService}.
     */
    @Override
    @AuditEvent(name = "UPDATE_MOVIE")
    public MovieDto update(UpdateMovieRequest request) {
        var movie = request.movie();
        checkForNullAndBlank(movie.id(), "id");
        idHandler.isValid(movie.id());
        Optional.ofNullable(movie.directorId())
                .ifPresent(idHandler::isValid);
        return origin.update(request);
    }
}
