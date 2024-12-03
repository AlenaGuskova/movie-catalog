package com.rntgroup.testingtask.moviecatalog.api.controller;

import com.rntgroup.testingtask.moviecatalog.api.request.CreateMovieRequest;
import com.rntgroup.testingtask.moviecatalog.api.request.UpdateMovieRequest;
import com.rntgroup.testingtask.moviecatalog.api.response.Response;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.ApiError;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.MovieDto;
import com.rntgroup.testingtask.moviecatalog.service.MovieService;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.OffsetDateTime;
import java.util.Collection;

import static org.springframework.shell.standard.ShellOption.NULL;

/**
 * {@inheritDoc}
 */
@Slf4j
@ShellComponent("movie")
@RequiredArgsConstructor
public class ShellMovieController implements MovieController {

    private final MovieService movieService;

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "list", prefix = "movie",
            value = "get full list of movies")
    public Response<Collection<MovieDto>> listMovies() {
        log.debug("Running the command to get list of movies");
        return Try.of(movieService::listMovies)
                .map(Response::success)
                .getOrElseGet(e -> Response.fail(createApiErrorFrom(e)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "get-by-genre", prefix = "movie",
            value = "get list of movies by genre. For example, get-by-genre --genre драма")
    public Response<Collection<MovieDto>> getByGenre(String genre) {
        log.debug("Running the command to get list of movies by genre:'{}'", genre);
        return Try.of(() -> movieService.getByGenre(genre))
                .map(Response::success)
                .getOrElseGet(e -> Response.fail(createApiErrorFrom(e)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "get-by-name", prefix = "movie",
            value = "get list of movies by name of director or actor. For example, get-by-name --name Нолан")
    public Response<Collection<MovieDto>> getByName(String name) {
        log.debug("Running the command to get list of movies by name of director or actor:'{}'", name);
        return Try.of(() -> movieService.getByName(name))
                .map(Response::success)
                .getOrElseGet(e -> Response.fail(createApiErrorFrom(e)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "get-by-prefix", prefix = "movie",
            value = "get list of movies by prefix in movie title. For example, get-by-prefix --prefix колец")
    public Response<Collection<MovieDto>> getByPrefixInTitle(String prefix) {
        log.debug("Running the command to get list of movies by prefix in movie title:'{}'", prefix);
        return Try.of(() -> movieService.getByPrefixInTitle(prefix))
                .map(Response::success)
                .getOrElseGet(e -> Response.fail(createApiErrorFrom(e)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "get", prefix = "movie",
            value = "get movie by id. For example, get --id e74318b9-0b1b-41c9-8c89-9779ce0261f3")
    public Response<MovieDto> getById(String id) {
        log.debug("Running the command to get movie by id:'{}'", id);
        return Try.of(() -> movieService.getById(id))
                .map(Response::success)
                .getOrElseGet(e -> Response.fail(createApiErrorFrom(e)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "delete", prefix = "movie",
            value = "delete movie by id. For example, delete --id 27a25c5e-de50-4176-944b-6058472681b3")
    public Response<Integer> delete(String id) {
        log.debug("Running the command to delete movie by id:'{}'", id);
        return Try.of(() -> movieService.delete(id))
                .map(Response::success)
                .getOrElseGet(e -> Response.fail(createApiErrorFrom(e)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "create", prefix = "movie",
            value = "create movie. For example, " +
                    "create --title \"Властелин колец: Две крепости\" " +
                    "--directorId 5483a2d9-6fa8-4ab9-b82a-cd032094dd12")
    public Response<MovieDto> create(String title,
                                     @ShellOption(defaultValue = NULL) String directorId) {
        var toCreate = new CreateMovieRequest(
                CreateMovieRequest.MovieRequest.builder()
                        .title(title)
                        .directorId(directorId)
                        .build()
        );
        log.debug("Running the command to create movie by request:'{}'", toCreate);
        return Try.of(() -> movieService.create(toCreate))
                .map(Response::success)
                .getOrElseGet(e -> Response.fail(createApiErrorFrom(e)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "update", prefix = "movie",
            value = "update movie. For example, " +
                    "update --id 2d21855e-cd44-48b5-9f38-b3d5786b52bd " +
                    "--title \"Властелин колец: Две крепости\"")
    public Response<MovieDto> update(String id, String title,
                                     @ShellOption(defaultValue = NULL) String directorId) {
        var toUpdate = new UpdateMovieRequest(
                UpdateMovieRequest.MovieRequest.builder()
                        .id(id)
                        .title(title)
                        .directorId(directorId)
                        .build()
        );
        log.debug("Running the command to update movie by request:'{}'", toUpdate);
        return Try.of(() -> movieService.update(toUpdate))
                .map(Response::success)
                .getOrElseGet(e -> Response.fail(createApiErrorFrom(e)));
    }

    private ApiError createApiErrorFrom(Throwable e) {
        return new ApiError(e.getLocalizedMessage(), OffsetDateTime.now());
    }
}