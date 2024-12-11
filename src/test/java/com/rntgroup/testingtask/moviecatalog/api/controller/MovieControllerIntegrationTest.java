package com.rntgroup.testingtask.moviecatalog.api.controller;

import java.util.List;
import com.rntgroup.testingtask.moviecatalog.IntegrationTest;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.ApiError;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.ApiError.ErrorRecord;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.MovieDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static com.rntgroup.testingtask.moviecatalog.domain.model.ActorCreator.createActorDto;
import static com.rntgroup.testingtask.moviecatalog.domain.model.DirectorCreator.createDirectorDto;
import static com.rntgroup.testingtask.moviecatalog.domain.model.GenreCreator.createGenreDto;
import static com.rntgroup.testingtask.moviecatalog.domain.model.MovieCreator.createMovieDto;
import static org.assertj.core.api.Assertions.assertThat;

class MovieControllerIntegrationTest extends IntegrationTest {

    @Autowired
    private MovieController underTest;

    @Nested
    class MovieControllerSuccessIntegrationTest {

        @Test
        @DisplayName("Should get movies")
        void shouldGetMovies() {
            var response = underTest.getMovies();

            var expected1 = createMovieDto("d890bbaf-f8e7-4c47-861b-e9368fabbd02", "1+1",
                    createDirectorDto("544e0aef-cb6f-413e-8e56-667c2f739e4d", "Оливье", "Накаш"),
                    List.of(createGenreDto("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                    List.of(createActorDto("64887d99-ff1d-48da-b066-7dbeb0f464f3", "Франсуа", "Клюзе"),
                            createActorDto("1abf568a-7106-49f1-b152-d002ba78df7d", "Омар", "Си")));
            var expected2 = createMovieDto("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Возвращение короля",
                    createDirectorDto("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"),
                    List.of(createGenreDto("80bd39cc-e349-48fb-9c06-373fe9d04768", "фэнтези"),
                            createGenreDto("27a33184-9500-4847-b83b-647de0538b2b", "приключения"),
                            createGenreDto("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                    List.of(createActorDto("3f825e80-f070-4829-85ce-62964a02854a", "Элайджа", "Вуд"),
                            createActorDto("4a42c006-c853-4795-b98e-3eefe6552940", "Вигго", "Мортенсен")));
            var expected3 = createMovieDto("e74318b9-0b1b-41c9-8c89-9779ce0261f3", "Унесённые призраками");
            assertThat(response.getContent())
                    .hasSize(3)
                    .containsExactlyInAnyOrder(expected1, expected2, expected3);
        }

        @Test
        @DisplayName("Should get movies by genre")
        void shouldGetMoviesByGenre() {
            var response = underTest.getByGenre("ДРАМА");

            var expected1 = createMovieDto("d890bbaf-f8e7-4c47-861b-e9368fabbd02", "1+1",
                    createDirectorDto("544e0aef-cb6f-413e-8e56-667c2f739e4d", "Оливье", "Накаш"),
                    List.of(createGenreDto("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                    List.of(createActorDto("64887d99-ff1d-48da-b066-7dbeb0f464f3", "Франсуа", "Клюзе"),
                            createActorDto("1abf568a-7106-49f1-b152-d002ba78df7d", "Омар", "Си")));
            var expected2 = createMovieDto("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Возвращение короля",
                    createDirectorDto("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"),
                    List.of(createGenreDto("80bd39cc-e349-48fb-9c06-373fe9d04768", "фэнтези"),
                            createGenreDto("27a33184-9500-4847-b83b-647de0538b2b", "приключения"),
                            createGenreDto("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                    List.of(createActorDto("3f825e80-f070-4829-85ce-62964a02854a", "Элайджа", "Вуд"),
                            createActorDto("4a42c006-c853-4795-b98e-3eefe6552940", "Вигго", "Мортенсен")));
            assertThat(response.getContent())
                    .hasSize(2)
                    .containsExactly(expected1, expected2);
        }

        @Test
        @DisplayName("Should get movies by fieldName")
        void shouldGetMoviesByName() {
            var response = underTest.getByName("  ДжексОн   ");

            var expected = createMovieDto("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Возвращение короля",
                    createDirectorDto("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"),
                    List.of(createGenreDto("80bd39cc-e349-48fb-9c06-373fe9d04768", "фэнтези"),
                            createGenreDto("27a33184-9500-4847-b83b-647de0538b2b", "приключения"),
                            createGenreDto("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                    List.of(createActorDto("3f825e80-f070-4829-85ce-62964a02854a", "Элайджа", "Вуд"),
                            createActorDto("4a42c006-c853-4795-b98e-3eefe6552940", "Вигго", "Мортенсен")));
            assertThat(response.getContent())
                    .hasSize(1)
                    .containsExactly(expected);
        }

        @Test
        @DisplayName("Should get movies by prefix")
        void shouldGetMoviesByPrefix() {
            var response = underTest.getByPrefixInTitle("  КОЛЕЦ   ");

            var expected = createMovieDto("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Возвращение короля",
                    createDirectorDto("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"),
                    List.of(createGenreDto("80bd39cc-e349-48fb-9c06-373fe9d04768", "фэнтези"),
                            createGenreDto("27a33184-9500-4847-b83b-647de0538b2b", "приключения"),
                            createGenreDto("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                    List.of(createActorDto("3f825e80-f070-4829-85ce-62964a02854a", "Элайджа", "Вуд"),
                            createActorDto("4a42c006-c853-4795-b98e-3eefe6552940", "Вигго", "Мортенсен")));
            assertThat(response.getContent())
                    .hasSize(1)
                    .containsExactly(expected);
        }

        @Test
        @DisplayName("Should get movie by id")
        void shouldGetMovieById() {
            var response = underTest.getById("2d21855e-cd44-48b5-9f38-b3d5786b52bd");

            var expectedMovie = createMovieDto("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Возвращение короля",
                    createDirectorDto("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"),
                    List.of(createGenreDto("80bd39cc-e349-48fb-9c06-373fe9d04768", "фэнтези"),
                            createGenreDto("27a33184-9500-4847-b83b-647de0538b2b", "приключения"),
                            createGenreDto("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                    List.of(createActorDto("3f825e80-f070-4829-85ce-62964a02854a", "Элайджа", "Вуд"),
                            createActorDto("4a42c006-c853-4795-b98e-3eefe6552940", "Вигго", "Мортенсен")));
            assertThat(response.getContent()).isEqualTo(expectedMovie);
        }

        @Test
        @DisplayName("Should delete movie")
        void shouldDeleteMovie() {
            assertThat(underTest.delete("2d21855e-cd44-48b5-9f38-b3d5786b52bd").getContent())
                    .isEqualTo(1);
        }

        @Test
        @DisplayName("Should save movie")
        void shouldSaveMovie() {
            var response = underTest.create("Новый Властелин колец", null);

            assertThat(response.getContent())
                    .extracting(MovieDto::title)
                    .isEqualTo("Новый Властелин колец");
        }

        @Test
        @DisplayName("Should save movie with director")
        void shouldSaveMovieWithDirector() {
            var response = underTest.create("Новый Властелин колец", "544e0aef-cb6f-413e-8e56-667c2f739e4d");

            assertThat(response.getContent())
                    .extracting(MovieDto::title, MovieDto::director)
                    .contains("Новый Властелин колец",
                            createDirectorDto("544e0aef-cb6f-413e-8e56-667c2f739e4d", "Оливье", "Накаш"));
        }

        @Test
        @DisplayName("Should update movie")
        void shouldUpdateMovie() {
            var response = underTest.update("2d21855e-cd44-48b5-9f38-b3d5786b52bd",
                    "Новый Властелин колец", null);

            var expectedMovie = createMovieDto("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Новый Властелин колец",
                    createDirectorDto("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"),
                    List.of(createGenreDto("80bd39cc-e349-48fb-9c06-373fe9d04768", "фэнтези"),
                            createGenreDto("27a33184-9500-4847-b83b-647de0538b2b", "приключения"),
                            createGenreDto("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                    List.of(createActorDto("3f825e80-f070-4829-85ce-62964a02854a", "Элайджа", "Вуд"),
                            createActorDto("4a42c006-c853-4795-b98e-3eefe6552940", "Вигго", "Мортенсен")));
            assertThat(response.getContent()).isEqualTo(expectedMovie);
        }

        @Test
        @DisplayName("Should update movie with director")
        void shouldUpdateMovieWithDirector() {
            var response = underTest.update("2d21855e-cd44-48b5-9f38-b3d5786b52bd",
                    "Новый Властелин колец", "1a3ea417-14b5-4525-bac8-58f7dfd613a6");

            var expectedMovie = createMovieDto("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Новый Властелин колец",
                    createDirectorDto("1a3ea417-14b5-4525-bac8-58f7dfd613a6", "Хаяо", "Миядзаки"),
                    List.of(createGenreDto("80bd39cc-e349-48fb-9c06-373fe9d04768", "фэнтези"),
                            createGenreDto("27a33184-9500-4847-b83b-647de0538b2b", "приключения"),
                            createGenreDto("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                    List.of(createActorDto("3f825e80-f070-4829-85ce-62964a02854a", "Элайджа", "Вуд"),
                            createActorDto("4a42c006-c853-4795-b98e-3eefe6552940", "Вигго", "Мортенсен")));
            assertThat(response.getContent()).isEqualTo(expectedMovie);
        }
    }

    @Nested
    class MovieControllerFailureIntegrationTest {

        @ParameterizedTest(name = "for genre: {0}")
        @ValueSource(strings = {"", "    "})
        @NullSource
        @DisplayName("Should get error when getting movie by invalid genre")
        void shouldGetErrorWhenGettingMovieByInvalidGenre(String genre) {
            var response = underTest.getByGenre(genre);

            assertThat(response.getApiError())
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
            var response = underTest.getByName(name);

            assertThat(response.getApiError())
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
            var response = underTest.getByPrefixInTitle(prefix);

            assertThat(response.getApiError())
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
            var response = underTest.getById(id);

            assertThat(response.getApiError())
                    .extracting(ApiError::message, ApiError::error)
                    .contains("Неправильные поля запроса", new ErrorRecord("id",
                            "Поле не должно быть пустым или отсутствовать: " + id)
                    );
        }

        @Test
        @DisplayName("Should get error when getting movie by non-uuid id")
        void shouldGetErrorWhenGettingMovieByNonUUIDId() {
            var response = underTest.getById("non-UUID");

            assertThat(response.getApiError())
                    .extracting(ApiError::message, ApiError::error)
                    .contains("Неправильные поля запроса", new ErrorRecord("id",
                            "Идентификатор должен быть UUID: non-UUID")
                    );
        }

        @Test
        @DisplayName("Should get error if unable to get movie by valid id")
        void shouldGetErrorIfUnableToGetMovieById() {
            var response = underTest.getById("5483a2d9-6fa8-4ab9-b82a-cd032094dd12");

            assertThat(response.getApiError())
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
            var response = underTest.delete(id);

            assertThat(response.getApiError())
                    .extracting(ApiError::message, ApiError::error)
                    .contains("Неправильные поля запроса", new ErrorRecord("id",
                            "Поле не должно быть пустым или отсутствовать: " + id)
                    );
        }

        @Test
        @DisplayName("Should get error when deleting movie by non-uuid id")
        void shouldGetErrorWhenDeletingMovieByNonUUIDId() {
            var response = underTest.delete("non-UUID");

            assertThat(response.getApiError())
                    .extracting(ApiError::message, ApiError::error)
                    .contains("Неправильные поля запроса", new ErrorRecord("id",
                            "Идентификатор должен быть UUID: non-UUID")
                    );
        }

        @Test
        @DisplayName("Should get error if unable to delete movie by valid id")
        void shouldGetErrorIfUnableToDeleteMovieById() {
            var response = underTest.delete("5483a2d9-6fa8-4ab9-b82a-cd032094dd12");

            assertThat(response.getApiError())
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
            var response = underTest.update(id, "", null);

            assertThat(response.getApiError())
                    .extracting(ApiError::message, ApiError::error)
                    .contains("Неправильные поля запроса", new ErrorRecord("id",
                            "Поле не должно быть пустым или отсутствовать: " + id)
                    );
        }

        @Test
        @DisplayName("Should get error when updating movie by non-uuid id")
        void shouldGetErrorWhenUpdatingMovieByNonUUIDId() {
            var response = underTest.update("non-UUID", "", null);

            assertThat(response.getApiError())
                    .extracting(ApiError::message, ApiError::error)
                    .contains("Неправильные поля запроса", new ErrorRecord("id",
                            "Идентификатор должен быть UUID: non-UUID")
                    );
        }

        @Test
        @DisplayName("Should get error if unable to update movie by valid id")
        void shouldGetErrorIfUnableToUpdateMovieById() {
            var response = underTest.update("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "", null);

            assertThat(response.getApiError())
                    .extracting(ApiError::message, ApiError::error)
                    .contains("Ресурс не найден", new ErrorRecord("id",
                            "Не удалось найти ресурс по идентификатору: 5483a2d9-6fa8-4ab9-b82a-cd032094dd12")
                    );
        }
    }
}