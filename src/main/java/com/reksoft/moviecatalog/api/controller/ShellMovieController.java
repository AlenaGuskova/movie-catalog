package com.reksoft.moviecatalog.api.controller;

import java.util.function.Supplier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reksoft.moviecatalog.api.request.CreateMovieRequest;
import com.reksoft.moviecatalog.api.request.UpdateMovieRequest;
import com.reksoft.moviecatalog.api.response.Response;
import com.reksoft.moviecatalog.service.MovieService;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import static org.springframework.shell.standard.ShellOption.NULL;

/**
 * {@inheritDoc}
 */
@ShellComponent
@RequiredArgsConstructor
public final class ShellMovieController implements MovieController {

    private final MovieService movieService;
    private final ObjectMapper objectMapper;
    private final GlobalErrorHandler exceptionHandler;

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "get-all", prefix = "movie", value = "get all movies")
    public String getMovies() {
        return createView(movieService::getMovies);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "get-by-genre", prefix = "movie",
        value = "get movies by genre. "
                    + "For example, get-by-genre --genre драма")
    public String getByGenre(String genre) {
        return createView(() -> movieService.getByGenre(genre));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "get-by-name", prefix = "movie",
        value = "get movies by name of director or actor. "
                    + "For example, get-by-name --name Нолан")
    public String getByName(String name) {
        return createView(() -> movieService.getByName(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "get-by-prefix", prefix = "movie",
        value = "get movies by prefix in movie title. "
                    + "For example, get-by-prefix --prefix колец")
    public String getByPrefixInTitle(String prefix) {
        return createView(() -> movieService.getByPrefixInTitle(prefix));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "get", prefix = "movie",
        value = "get movie by id. For example, "
                    + "get --id e74318b9-0b1b-41c9-8c89-9779ce0261f3")
    public String getById(String id) {
        return createView(() -> movieService.getById(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "delete", prefix = "movie",
        value = "delete movie by id. For example, "
                    + "delete --id 27a25c5e-de50-4176-944b-6058472681b3")
    public String delete(String id) {
        return createView(() -> movieService.delete(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "create", prefix = "movie",
        value = "create movie. For example, "
                    + "create --title \"Властелин колец: Две крепости\" "
                    + "--directorId 5483a2d9-6fa8-4ab9-b82a-cd032094dd12")
    public String create(String title,
                                     @ShellOption(defaultValue = NULL) String directorId) {
        var requestToCreateMovie = new CreateMovieRequest(
            new CreateMovieRequest.MovieRequest(null, title, directorId)
        );
        return createView(() -> movieService.create(requestToCreateMovie));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ShellMethod(key = "update", prefix = "movie",
        value = "update movie. For example, "
                    + "update --id 2d21855e-cd44-48b5-9f38-b3d5786b52bd "
                    + "--title \"Властелин колец: Две крепости\"")
    public String update(String id, String title,
                                     @ShellOption(defaultValue = NULL) String directorId) {
        var requestToUpdateMovie = new UpdateMovieRequest(
            new UpdateMovieRequest.MovieRequest(id, title, directorId)
        );
        return createView(() -> movieService.update(requestToUpdateMovie));
    }

    private <T> String createView(Supplier<T> action) {
        var response = Try.of(action::get)
                           .map(Response::success)
                           .getOrElseGet(e -> Response.failure(exceptionHandler.convert(e)));
        return Try.of(() -> objectMapper.writeValueAsString(response))
                   .getOrNull();
    }
}