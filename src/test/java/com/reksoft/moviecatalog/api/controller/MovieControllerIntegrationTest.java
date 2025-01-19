package com.reksoft.moviecatalog.api.controller;

import java.util.List;
import java.util.function.Supplier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reksoft.moviecatalog.IntegrationTest;
import com.reksoft.moviecatalog.api.response.Response;
import com.reksoft.moviecatalog.api.response.dto.ApiError;
import com.reksoft.moviecatalog.api.response.dto.ApiError.ErrorRecord;
import io.vavr.control.Try;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static com.reksoft.moviecatalog.domain.model.MovieCreator.getMoviesFromDB;
import static com.reksoft.moviecatalog.domain.model.MovieCreator.getPopularMovie;
import static org.assertj.core.api.Assertions.assertThat;

class MovieControllerIntegrationTest extends IntegrationTest {

    @Autowired
    private MovieController underTest;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class MovieControllerSuccessIntegrationTest {

        @Test
        @DisplayName("Should get movies")
        void shouldGetMovies() {
            var response = underTest.getMovies();
            assertThat(response)
                .isEqualTo(toJson(getMoviesFromDB()));
        }

        @Test
        @DisplayName("Should get movies by genre")
        void shouldGetMoviesByGenre() {
            var response = underTest.getByGenre("ДРАМА");

            assertThat(response)
                .isEqualTo(toJson(List.of(getMoviesFromDB().get(0), getMoviesFromDB().get(1))));
        }

        @Test
        @DisplayName("Should get movies by fieldName")
        void shouldGetMoviesByName() {
            var response = underTest.getByName("  ДжексОн   ");

            assertThat(response)
                .isEqualTo(toJson(List.of(getPopularMovie())));
        }

        @Test
        @DisplayName("Should get movies by prefix")
        void shouldGetMoviesByPrefix() {
            var response = underTest.getByPrefixInTitle("  КОЛЕЦ   ");

            assertThat(response)
                .isEqualTo(toJson(List.of(getPopularMovie())));
        }

        @Test
        @DisplayName("Should get movie by id")
        void shouldGetMovieById() throws JsonProcessingException {
            var response = underTest.getById("2d21855e-cd44-48b5-9f38-b3d5786b52bd");

            assertThat(objectMapper.readTree(response))
                .isEqualTo(objectMapper.readTree(toJson(getPopularMovie())));
        }

        @Test
        @DisplayName("Should delete movie")
        void shouldDeleteMovie() {
            assertThat(underTest.delete("2d21855e-cd44-48b5-9f38-b3d5786b52bd"))
                .isEqualTo(toJson(1));
        }

        @Test
        @DisplayName("Should save movie")
        void shouldSaveMovie() {
            var response = underTest.create("Новый Властелин колец", null);

            assertThat(response).contains("Новый Властелин колец");
        }

        @Test
        @DisplayName("Should save movie with director")
        void shouldSaveMovieWithDirector() {
            var response = underTest.create("Новый Властелин колец", "544e0aef-cb6f-413e-8e56-667c2f739e4d");

            assertThat(response)
                .contains("Новый Властелин колец")
                .contains("544e0aef-cb6f-413e-8e56-667c2f739e4d")
                .contains("Оливье")
                .contains("Накаш");
        }

        @Test
        @DisplayName("Should update movie")
        void shouldUpdateMovie() {
            var response = underTest.update("2d21855e-cd44-48b5-9f38-b3d5786b52bd",
                "Новый Властелин колец", null);

            assertThat(response)
                .contains("Новый Властелин колец")
                .doesNotContain("5483a2d9-6fa8-4ab9-b82a-cd032094dd12");
        }

        @Test
        @DisplayName("Should update movie with director")
        void shouldUpdateMovieWithDirector() {
            var response = underTest.update("2d21855e-cd44-48b5-9f38-b3d5786b52bd",
                "Новый Властелин колец", "1a3ea417-14b5-4525-bac8-58f7dfd613a6");

            assertThat(response)
                .contains("Новый Властелин колец")
                .contains("1a3ea417-14b5-4525-bac8-58f7dfd613a6")
                .doesNotContain("5483a2d9-6fa8-4ab9-b82a-cd032094dd12");
        }

        private String toJson(Object object) {
            return Try.of(() -> objectMapper.writeValueAsString(Response.success(object)))
                       .get();
        }
    }

    @Nested
    class MovieControllerFailureIntegrationTest {

        @ParameterizedTest(name = "for genre: {0}")
        @ValueSource(strings = {"", "    "})
        @NullSource
        @DisplayName("Should get error when getting movie by invalid genre")
        void shouldGetErrorWhenGettingMovieByInvalidGenre(String genre) {
            assertThat(getApiError(() -> underTest.getByGenre(genre)))
                .extracting(ApiError::message, ApiError::error)
                .contains("Неправильные поля запроса", new ErrorRecord("genre",
                    "Поле не должно быть пустым или отсутствовать: " + genre)
                );
        }

        @ParameterizedTest(name = "for fieldName: {0}")
        @ValueSource(strings = {"", "    "})
        @NullSource
        @DisplayName("Should get error when getting movie by invalid fieldName")
        void shouldGetErrorWhenGettingMovieByInvalidName(String name) {
            assertThat(getApiError(() -> underTest.getByName(name)))
                .extracting(ApiError::message, ApiError::error)
                .contains("Неправильные поля запроса", new ErrorRecord("name",
                    "Поле не должно быть пустым или отсутствовать: " + name)
                );
        }

        @ParameterizedTest(name = "for prefix: {0}")
        @ValueSource(strings = {"", "    "})
        @NullSource
        @DisplayName("Should get error when getting movie by invalid prefix")
        void shouldGetErrorWhenGettingMovieByInvalidPrefix(String prefix) {
            assertThat(getApiError(() -> underTest.getByPrefixInTitle(prefix)))
                .extracting(ApiError::message, ApiError::error)
                .contains("Неправильные поля запроса", new ErrorRecord("prefix",
                    "Поле не должно быть пустым или отсутствовать: " + prefix)
                );
        }

        @ParameterizedTest(name = "for id: {0}")
        @ValueSource(strings = {"", "    "})
        @NullSource
        @DisplayName("Should get error when getting movie by invalid id")
        void shouldGetErrorWhenGettingMovieByInvalidId(String id) {
            assertThat(getApiError(() -> underTest.getById(id)))
                .extracting(ApiError::message, ApiError::error)
                .contains("Неправильные поля запроса", new ErrorRecord("id",
                    "Поле не должно быть пустым или отсутствовать: " + id)
                );
        }

        @Test
        @DisplayName("Should get error when getting movie by non-uuid id")
        void shouldGetErrorWhenGettingMovieByNonUUIDId() {
            assertThat(getApiError(() -> underTest.getById("non-UUID")))
                .extracting(ApiError::message, ApiError::error)
                .contains("Неправильные поля запроса", new ErrorRecord("id",
                    "Идентификатор должен быть UUID: non-UUID")
                );
        }

        @Test
        @DisplayName("Should get error if unable to get movie by valid id")
        void shouldGetErrorIfUnableToGetMovieById() {
            assertThat(getApiError(() -> underTest.getById("5483a2d9-6fa8-4ab9-b82a-cd032094dd12")))
                .extracting(ApiError::message, ApiError::error)
                .contains("Ресурс не найден", new ErrorRecord("id",
                    "Не удалось найти ресурс по идентификатору: 5483a2d9-6fa8-4ab9-b82a-cd032094dd12")
                );
        }

        @ParameterizedTest(name = "for id: {0}")
        @ValueSource(strings = {"", "    "})
        @NullSource
        @DisplayName("Should get error when deleting movie by invalid id")
        void shouldGetErrorWhenDeletingMovieByInvalidId(String id) {
            assertThat(getApiError(() -> underTest.delete(id)))
                .extracting(ApiError::message, ApiError::error)
                .contains("Неправильные поля запроса", new ErrorRecord("id",
                    "Поле не должно быть пустым или отсутствовать: " + id)
                );
        }

        @Test
        @DisplayName("Should get error when deleting movie by non-uuid id")
        void shouldGetErrorWhenDeletingMovieByNonUUIDId() {
            assertThat(getApiError(() -> underTest.delete("non-UUID")))
                .extracting(ApiError::message, ApiError::error)
                .contains("Неправильные поля запроса", new ErrorRecord("id",
                    "Идентификатор должен быть UUID: non-UUID")
                );
        }

        @Test
        @DisplayName("Should get error if unable to delete movie by valid id")
        void shouldGetErrorIfUnableToDeleteMovieById() {
            assertThat(getApiError(() -> underTest.delete("5483a2d9-6fa8-4ab9-b82a-cd032094dd12")))
                .extracting(ApiError::message, ApiError::error)
                .contains("Ресурс не найден", new ErrorRecord("id",
                    "Не удалось найти ресурс по идентификатору: 5483a2d9-6fa8-4ab9-b82a-cd032094dd12")
                );
        }

        @ParameterizedTest(name = "for id: {0}")
        @ValueSource(strings = {"", "    "})
        @NullSource
        @DisplayName("Should get error when updating movie by invalid id")
        void shouldGetErrorWhenUpdatingMovieByInvalidId(String id) {
            assertThat(getApiError(() -> underTest.update(id, "", null)))
                .extracting(ApiError::message, ApiError::error)
                .contains("Неправильные поля запроса", new ErrorRecord("id",
                    "Поле не должно быть пустым или отсутствовать: " + id)
                );
        }

        @Test
        @DisplayName("Should get error when updating movie by non-uuid id")
        void shouldGetErrorWhenUpdatingMovieByNonUUIDId() {
            assertThat(getApiError(() -> underTest.update("non-UUID", "", null)))
                .extracting(ApiError::message, ApiError::error)
                .contains("Неправильные поля запроса", new ErrorRecord("id",
                    "Идентификатор должен быть UUID: non-UUID")
                );
        }

        @Test
        @DisplayName("Should get error if unable to update movie by valid id")
        void shouldGetErrorIfUnableToUpdateMovieById() {
            assertThat(getApiError(() -> underTest.update("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "", null)))
                .extracting(ApiError::message, ApiError::error)
                .contains("Ресурс не найден", new ErrorRecord("id",
                    "Не удалось найти ресурс по идентификатору: 5483a2d9-6fa8-4ab9-b82a-cd032094dd12")
                );
        }

        private ApiError getApiError(Supplier<String> action) {
            return Try.of(action::get)
                       .mapTry(response -> objectMapper.readValue(response, Response.class))
                       .map(Response::apiError)
                       .get();
        }
    }
}