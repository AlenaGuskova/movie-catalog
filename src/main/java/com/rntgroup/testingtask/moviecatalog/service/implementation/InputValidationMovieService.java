package com.rntgroup.testingtask.moviecatalog.service.implementation;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import com.rntgroup.testingtask.moviecatalog.api.request.CreateMovieRequest;
import com.rntgroup.testingtask.moviecatalog.api.request.CreateMovieRequest.MovieRequest;
import com.rntgroup.testingtask.moviecatalog.api.request.UpdateMovieRequest;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.MovieDto;
import com.rntgroup.testingtask.moviecatalog.service.MovieService;
import com.rntgroup.testingtask.moviecatalog.service.utils.IdHandler;
import com.rntgroup.testingtask.moviecatalog.service.exception.NullAndBlankValidationException;
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
    public Collection<MovieDto> getMovies() {
        return origin.getMovies();
    }

    /**
     * {@inheritDoc}
     * It checks the genre for null and blank and then transfer
     * the responsibility of getting movies by genre to another {@link MovieService}.
     */
    @Override
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
    public MovieDto update(UpdateMovieRequest request) {
        var movie = request.movie();
        checkForNullAndBlank(movie.id(), "id");
        idHandler.isValid(movie.id());
        Optional.of(movie)
                .map(UpdateMovieRequest.MovieRequest::directorId)
                .ifPresent(idHandler::isValid);
        return origin.update(request);
    }
}
