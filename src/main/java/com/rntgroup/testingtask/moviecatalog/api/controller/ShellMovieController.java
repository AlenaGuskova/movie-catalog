package com.rntgroup.testingtask.moviecatalog.api.controller;

import com.rntgroup.testingtask.moviecatalog.api.response.Response;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.ApiError;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.Director;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.Movie;
import com.rntgroup.testingtask.moviecatalog.service.MovieService;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.OffsetDateTime;
import java.util.Collection;

@Slf4j
@ShellComponent
@RequiredArgsConstructor
public class ShellMovieController implements MovieController {

    private final MovieService movieService;

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "list-movies", value = "get full list of movies", prefix = "movie")
    public Response<Collection<Movie>> listMovies() {
        log.debug("Running the command to get list of movies");
        return Try.of(movieService::listMovies)
                .map(Response::success)
                .getOrElseGet(e -> Response.fail(getApiError(e)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "get-by-genre", prefix = "movie",
            value = "get list of movies by genre. For example, get-by-genre --genre драма")
    public Response<Collection<Movie>> getByGenre(@ShellOption(defaultValue = "") String genre) {
        log.debug("Running the command to get list of movies by genre: {}", genre);
        return Try.of(() -> movieService.getByGenre(genre))
                .map(Response::success)
                .getOrElseGet(e -> Response.fail(getApiError(e)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "get-by-name", prefix = "movie",
            value = "get list of movies by name of director or actor. For example, get-by-name --name Нолан")
    public Response<Collection<Movie>> getByName(@ShellOption(defaultValue = "") String name) {
        log.debug("Running the command to get list of movies by name of director or actor: {}", name);
        return Try.of(() -> movieService.getByName(name))
                .map(Response::success)
                .getOrElseGet(e -> Response.fail(getApiError(e)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "get-by-prefix-in-title", prefix = "movie",
            value = "get list of movies by prefix in movie title. For example, get-by-prefix-in-title --prefix колец")
    public Response<Collection<Movie>> getByPrefixInTitle(@ShellOption String prefix) {
        log.debug("Running the command to get list of movies by prefix in movie title: {}", prefix);
        return Try.of(() -> movieService.getByPrefixInTitle(prefix))
                .map(Response::success)
                .getOrElseGet(e -> Response.fail(getApiError(e)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "get-by-id", prefix = "movie",
            value = "get movie by ID. For example, get-by-id --id VONQUE7X4WTQ4UQK4GLL723UVHD3MENBN")
    public Response<Movie> getById(@ShellOption String id) {
        log.debug("Running the command to get movie by ID: {}", id);
        return Try.of(() -> movieService.getById(id))
                .map(Response::success)
                .getOrElseGet(e -> Response.fail(getApiError(e)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "delete-by-id", prefix = "movie",
            value = "delete movie by ID. For example, delete-by-id --id VVSAYW5BCLQGODBA6OMW3DVYBT53ACQMQ")
    public Response<Integer> delete(@ShellOption String id) {
        log.debug("Running the command to delete movie by ID: {}", id);
        return Try.of(() -> movieService.delete(id))
                .map(Response::success)
                .getOrElseGet(e -> Response.fail(getApiError(e)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "create", prefix = "movie",
            value = "create movie. For example, " +
                    "create --title \"Властелин колец: Две крепости\" " +
                    "--directorId VUBMYE5Y3DWAV6DPXYWXR7HD6P3T7QXUU")
    public Response<Movie> create(@ShellOption String title,
                                  @ShellOption(defaultValue = "") String directorId) {
        log.debug("Running the command to create movie by title: {} and directorId: {}", title, directorId);
        var toCreate = Movie.builder()
                .title(title)
                .director(Director.builder()
                        .id(directorId)
                        .build()
                )
                .build();
        return Try.of(() -> movieService.create(toCreate))
                .map(Response::success)
                .getOrElseGet(e -> Response.fail(getApiError(e)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "update", prefix = "movie",
            value = "update movie. For example, " +
                    "update --id VONQUE7X4WTQ4UQK4GLL723UVHD3MENBN " +
                    "--title \"Властелин колец: Две крепости\"")
    public Response<Movie> update(@ShellOption String id,
                                  @ShellOption String title,
                                  @ShellOption(defaultValue = "") String directorId) {
        log.debug("Running the command to update movie by id: {}", id);
        var toCreate = Movie.builder()
                .title(title)
                .director(Director.builder()
                        .id(directorId)
                        .build()
                )
                .build();
        return Try.of(() -> movieService.update(id, toCreate))
                .map(Response::success)
                .getOrElseGet(e -> Response.fail(getApiError(e)));
    }

    private ApiError getApiError(Throwable e) {
        return new ApiError(e.getLocalizedMessage(), OffsetDateTime.now());
    }
}