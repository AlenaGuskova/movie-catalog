package com.rntgroup.testingtask.moviecatalog.service.implementation;

import com.rntgroup.testingtask.moviecatalog.api.request.CreateMovieRequest;
import com.rntgroup.testingtask.moviecatalog.api.request.UpdateMovieRequest;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.MovieDto;
import com.rntgroup.testingtask.moviecatalog.domain.utils.IdHandler;
import com.rntgroup.testingtask.moviecatalog.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

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

    @Override
    public Collection<MovieDto> listMovies() {
        return origin.listMovies();
    }

    @Override
    public Collection<MovieDto> getByGenre(String input) {
        checkForNullAndBlank(input, "Invalid genre: " + input);
        var genre = input.toLowerCase(Locale.ROOT).trim();
        return origin.getByGenre(genre);
    }

    @Override
    public Collection<MovieDto> getByName(String input) {
        checkForNullAndBlank(input, "Invalid human's name: " + input);
        var name = input.toLowerCase(Locale.ROOT).trim();
        return origin.getByName(name);
    }

    @Override
    public Collection<MovieDto> getByPrefixInTitle(String input) {
        checkForNullAndBlank(input, "Invalid prefix in movie title: " + input);
        var prefix = input.toLowerCase(Locale.ROOT).trim();
        return origin.getByPrefixInTitle(prefix);
    }

    private void checkForNullAndBlank(String input, String exceptionMessage) {
        if (Objects.isNull(input) || input.isBlank()) {
            log.warn(exceptionMessage);
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    @Override
    public MovieDto getById(String id) {
        idHandler.isValid(id);
        return origin.getById(id);
    }

    @Override
    public int delete(String id) {
        idHandler.isValid(id);
        return origin.delete(id);
    }

    @Override
    public MovieDto create(CreateMovieRequest request) {
        var resource = request.getResource();
        Optional.of(resource)
                .map(CreateMovieRequest.MovieRequest::getDirectorId)
                .ifPresent(idHandler::isValid);
        var toCreate = new CreateMovieRequest(resource
                .toBuilder()
                .id(idHandler.generateId())
                .build());
        return origin.create(toCreate);
    }

    @Override
    public MovieDto update(UpdateMovieRequest request) {
        var resource = request.getResource();
        idHandler.isValid(resource.getId());
        Optional.of(resource)
                .map(UpdateMovieRequest.MovieRequest::getDirectorId)
                .ifPresent(idHandler::isValid);
        return origin.update(request);
    }
}
