package com.rntgroup.testingtask.moviecatalog.service;

import com.rntgroup.testingtask.moviecatalog.api.response.dto.Movie;
import com.rntgroup.testingtask.moviecatalog.domain.dao.DAO;
import com.rntgroup.testingtask.moviecatalog.domain.dao.FakeDirectorDAO;
import com.rntgroup.testingtask.moviecatalog.domain.dao.FakeMovieDAO;
import com.rntgroup.testingtask.moviecatalog.domain.dao.MovieDAO;
import com.rntgroup.testingtask.moviecatalog.domain.mapper.MovieEntityMapper;
import com.rntgroup.testingtask.moviecatalog.domain.mapper.MovieEntityMapperImpl;
import com.rntgroup.testingtask.moviecatalog.domain.model.DirectorEntity;
import com.rntgroup.testingtask.moviecatalog.domain.utils.IdHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.rntgroup.testingtask.moviecatalog.domain.model.ActorCreator.createActor;
import static com.rntgroup.testingtask.moviecatalog.domain.model.ActorCreator.createActorEntity;
import static com.rntgroup.testingtask.moviecatalog.domain.model.DirectorCreator.createDirector;
import static com.rntgroup.testingtask.moviecatalog.domain.model.DirectorCreator.createDirectorEntity;
import static com.rntgroup.testingtask.moviecatalog.domain.model.GenreCreator.createGenre;
import static com.rntgroup.testingtask.moviecatalog.domain.model.GenreCreator.createGenreEntity;
import static com.rntgroup.testingtask.moviecatalog.domain.model.MovieCreator.createMovie;
import static com.rntgroup.testingtask.moviecatalog.domain.model.MovieCreator.createMovieEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    private final MovieDAO movieDAO = new FakeMovieDAO();
    private final DAO<DirectorEntity> directorDAO = new FakeDirectorDAO();
    private final IdHandler idHandler = mock(IdHandler.class);
    private final MovieEntityMapper movieEntityMapper = new MovieEntityMapperImpl();

    private MovieServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new MovieServiceImpl(movieDAO, directorDAO, idHandler, movieEntityMapper);
    }

    @Test
    @DisplayName("Should return a list of movies successfully")
    void shouldReturnListOfMoviesSuccessfully() {
        movieDAO.save(
                createMovieEntity("1", "1+1",
                        createDirectorEntity("1", "Оливье", "Накаш"),
                        List.of(createGenreEntity("1", "драма")),
                        List.of(
                                createActorEntity("1", "Франсуа", "Клюзе"),
                                createActorEntity("2", "Омар", "Си")
                        )
                )
        );
        movieDAO.save(
                createMovieEntity("2", "Властелин колец: Братство Кольца",
                        createDirectorEntity("2", "Питер", "Джексон"),
                        List.of(createGenreEntity("2", "фэнтези")),
                        List.of(
                                createActorEntity("3", "Элайджа", "Вуд"),
                                createActorEntity("4", "Иэн", "Маккеллен")
                        )
                )
        );

        var actualMovies = underTest.listMovies();

        var expectedMovie1 = createMovie("1", "1+1",
                createDirector("1", "Оливье", "Накаш"),
                List.of(createGenre("1", "драма")),
                List.of(
                        createActor("1", "Франсуа", "Клюзе"),
                        createActor("2", "Омар", "Си")
                )
        );
        var expectedMovie2 = createMovie("2", "Властелин колец: Братство Кольца",
                createDirector("2", "Питер", "Джексон"),
                List.of(createGenre("2", "фэнтези")),
                List.of(
                        createActor("3", "Элайджа", "Вуд"),
                        createActor("4", "Иэн", "Маккеллен")
                )
        );
        assertThat(actualMovies).hasSize(2)
                .containsExactly(expectedMovie1, expectedMovie2);
    }

    @Test
    @DisplayName("Should return a list of movies by genre successfully")
    void shouldReturnListOfMoviesByGenreSuccessfully() {
        movieDAO.save(
                createMovieEntity("1", "1+1",
                        createDirectorEntity("1", "Оливье", "Накаш"),
                        List.of(createGenreEntity("1", "драма"),
                                createGenreEntity("2", "комедия")),
                        List.of(createActorEntity("1", "Франсуа", "Клюзе"),
                                createActorEntity("2", "Омар", "Си"))
                )
        );
        movieDAO.save(
                createMovieEntity("2", "Властелин колец: Возвращение короля",
                        createDirectorEntity("2", "Питер", "Джексон"),
                        List.of(createGenreEntity("3", "фэнтези"),
                                createGenreEntity("4", "приключения"),
                                createGenreEntity("5", "драма")),
                        List.of(createActorEntity("3", "Элайджа", "Вуд"),
                                createActorEntity("4", "Вигго", "Мортенсен"))
                )
        );
        movieDAO.save(
                createMovieEntity("3", "Унесённые призраками",
                        createDirectorEntity("3", "Хаяо", "Миядзаки"),
                        List.of(createGenreEntity("6", "аниме")),
                        List.of(createActorEntity("5", "Руми", "Хиираги"),
                                createActorEntity("6", "Мию", "Ирино"))
                )
        );

        var actualMovies = underTest.getByGenre("драма");

        var expectedMovie1 = createMovie("1", "1+1",
                null, null, null);
        var expectedMovie2 = createMovie("2", "Властелин колец: Возвращение короля",
                null, null, null);

        assertThat(actualMovies).hasSize(2)
                .containsExactly(expectedMovie1, expectedMovie2);
    }

    @Test
    @DisplayName("Should return an empty list of movies by non-exiting genre")
    void shouldReturnEmptyListOfMoviesByNonExistingGenre() {
        movieDAO.save(
                createMovieEntity("1", "1+1",
                        createDirectorEntity("1", "Оливье", "Накаш"),
                        List.of(createGenreEntity("1", "драма"),
                                createGenreEntity("2", "комедия")),
                        List.of(createActorEntity("1", "Франсуа", "Клюзе"),
                                createActorEntity("2", "Омар", "Си"))
                )
        );
        movieDAO.save(
                createMovieEntity("2", "Властелин колец: Возвращение короля",
                        createDirectorEntity("2", "Питер", "Джексон"),
                        List.of(createGenreEntity("3", "фэнтези"),
                                createGenreEntity("4", "приключения"),
                                createGenreEntity("5", "драма")),
                        List.of(createActorEntity("3", "Элайджа", "Вуд"),
                                createActorEntity("4", "Вигго", "Мортенсен"))
                )
        );

        var actualMovies = underTest.getByGenre("военный");

        assertThat(actualMovies).isEmpty();
    }

    @ParameterizedTest(name = "for genre: {0}")
    @ValueSource(strings = {"", " "})
    @NullSource
    @DisplayName("Should return an empty list of movies by nullable genre")
    void shouldReturnEmptyListOfMoviesByNullableGenre(String genre) {
        var actualMovies = underTest.getByGenre(genre);

        assertThat(actualMovies).isEmpty();
    }

    @Test
    @DisplayName("Should return a list of movies by name successfully")
    void shouldReturnListOfMoviesByNameSuccessfully() {
        movieDAO.save(
                createMovieEntity("1", "1+1",
                        createDirectorEntity("1", "Оливье", "Накаш"),
                        List.of(createGenreEntity("1", "драма"),
                                createGenreEntity("2", "комедия")),
                        List.of(createActorEntity("1", "Франсуа", "Клюзе"),
                                createActorEntity("2", "Омар", "Си"))
                )
        );
        movieDAO.save(
                createMovieEntity("2", "Властелин колец: Возвращение короля",
                        createDirectorEntity("2", "Питер", "Джексон"),
                        List.of(createGenreEntity("3", "фэнтези"),
                                createGenreEntity("4", "приключения"),
                                createGenreEntity("5", "драма")),
                        List.of(createActorEntity("3", "Элайджа", "Вуд"),
                                createActorEntity("4", "Вигго", "Мортенсен"))
                )
        );

        var actualMovies = underTest.getByName("Джексон");

        assertThat(actualMovies).hasSize(1)
                .containsExactly(createMovie("2", "Властелин колец: Возвращение короля",
                        null, null, null));
    }

    @ParameterizedTest(name = "for name: {0}")
    @ValueSource(strings = {"", " "})
    @NullSource
    @DisplayName("Should return an empty list of movies by nullable name")
    void shouldReturnEmptyListOfMoviesByNullableName(String name) {
        var actualMovies = underTest.getByName(name);

        assertThat(actualMovies).isEmpty();
    }

    @Test
    @DisplayName("Should return a list of movies by prefix in movie title successfully")
    void shouldReturnListOfMoviesByPrefixInTitleSuccessfully() {
        movieDAO.save(
                createMovieEntity("1", "Властелин колец: Братство Кольца",
                        createDirectorEntity("1", "Питер", "Джексон"),
                        List.of(createGenreEntity("3", "фэнтези"),
                                createGenreEntity("4", "приключения"),
                                createGenreEntity("5", "драма")),
                        List.of(createActorEntity("3", "Элайджа", "Вуд"),
                                createActorEntity("4", "Вигго", "Мортенсен"))
                )
        );
        movieDAO.save(
                createMovieEntity("2", "Властелин колец: Возвращение короля",
                        createDirectorEntity("1", "Питер", "Джексон"),
                        List.of(createGenreEntity("3", "фэнтези"),
                                createGenreEntity("4", "приключения"),
                                createGenreEntity("5", "драма")),
                        List.of(createActorEntity("3", "Элайджа", "Вуд"),
                                createActorEntity("4", "Вигго", "Мортенсен"))
                )
        );

        var actualMovies = underTest.getByPrefixInTitle("колец");

        var expectedMovie1 = createMovie("1", "Властелин колец: Братство Кольца",
                null, null, null);
        var expectedMovie2 = createMovie("2", "Властелин колец: Возвращение короля",
                null, null, null);

        assertThat(actualMovies).hasSize(2)
                .containsExactly(expectedMovie1, expectedMovie2);
    }

    @ParameterizedTest(name = "for prefix: {0}")
    @ValueSource(strings = {"", " "})
    @NullSource
    @DisplayName("Should return an empty list of movies by nullable prefix in movie title")
    void shouldReturnEmptyListOfMoviesByNullablePrefix(String prefix) {
        var actualMovies = underTest.getByPrefixInTitle(prefix);

        assertThat(actualMovies).isEmpty();
    }

    @Test
    @DisplayName("Should return movie by id successfully")
    void shouldReturnMovieById() {
        when(idHandler.isValid("2")).thenReturn(true);

        movieDAO.save(
                createMovieEntity("1", "Властелин колец: Братство Кольца",
                        createDirectorEntity("1", "Питер", "Джексон"),
                        List.of(createGenreEntity("3", "фэнтези"),
                                createGenreEntity("4", "приключения"),
                                createGenreEntity("5", "драма")),
                        List.of(createActorEntity("3", "Элайджа", "Вуд"),
                                createActorEntity("4", "Вигго", "Мортенсен"))
                )
        );
        movieDAO.save(
                createMovieEntity("2", "Властелин колец: Возвращение короля",
                        createDirectorEntity("1", "Питер", "Джексон"),
                        List.of(createGenreEntity("3", "фэнтези"),
                                createGenreEntity("4", "приключения"),
                                createGenreEntity("5", "драма")),
                        List.of(createActorEntity("3", "Элайджа", "Вуд"),
                                createActorEntity("4", "Вигго", "Мортенсен"))
                )
        );

        var actualMovie = underTest.getById("2");

        var expectedMovie = createMovie("2", "Властелин колец: Возвращение короля",
                createDirector("1", "Питер", "Джексон"),
                List.of(createGenre("3", "фэнтези"),
                        createGenre("4", "приключения"),
                        createGenre("5", "драма")),
                List.of(createActor("3", "Элайджа", "Вуд"),
                        createActor("4", "Вигго", "Мортенсен"))
        );

        assertThat(actualMovie).isEqualTo(expectedMovie);
    }

    @Test
    @DisplayName("Should return empty movie information by id when id is invalid")
    void shouldReturnEmptyMovieInformationByInvalidId() {
        when(idHandler.isValid("invalid")).thenReturn(false);

        var actualMovie = underTest.getById("invalid");

        assertThat(actualMovie).isEqualTo(createMovie("invalid", null, null, null, null));
    }

    @Test
    @DisplayName("Should delete movie by id")
    void shouldDeleteMovieById() {
        when(idHandler.isValid("valid")).thenReturn(true);

        assertThat(underTest.delete("valid")).isEqualTo(1);
    }

    @Test
    @DisplayName("Should not delete movie by id when id is invalid")
    void shouldNotDeleteMovieByIdByInvalidId() {
        when(idHandler.isValid("invalid")).thenReturn(false);

        assertThat(underTest.delete("invalid")).isEqualTo(0);
    }

    @Test
    @DisplayName("Should update movie by id")
    void shouldUpdateMovieById() {
        when(idHandler.isValid("1")).thenReturn(true);
        when(idHandler.isValid("directorId")).thenReturn(true);

        directorDAO.save(createDirectorEntity("directorId", "Новый Питер", "Новый Джексон"));
        movieDAO.save(
                createMovieEntity("1", "Властелин колец: Братство Кольца",
                        createDirectorEntity("1", "Питер", "Джексон"),
                        null, null)
        );

        var toUpdate = Movie.builder()
                .title("Новый Властелин колец")
                .director(createDirector("directorId"))
                .build();
        var actualMovie = underTest.update("1", toUpdate);

        assertThat(actualMovie).isEqualTo(
                createMovie("1",
                        "Новый Властелин колец",
                        createDirector("directorId", "Новый Питер", "Новый Джексон"),
                        null, null)
        );
    }

    @Test
    @DisplayName("Should save movie successfully")
    void shouldSaveMovie() {
        when(idHandler.isValid("directorId")).thenReturn(true);
        when(idHandler.generateId()).thenReturn("newId");

        directorDAO.save(createDirectorEntity("directorId", "Питер", "Джексон"));
        assertThat(movieDAO.findAll()).hasSize(0);

        var toCreate = Movie.builder()
                .id("1")
                .title("Новый Властелин колец")
                .director(createDirector("directorId"))
                .build();
        var actualMovie = underTest.create(toCreate);

        assertThat(actualMovie).isEqualTo(
                createMovie("newId",
                        "Новый Властелин колец",
                        createDirector("directorId", "Питер", "Джексон"),
                        null, null)
        );
        assertThat(movieDAO.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("Should return empty movie information by id when id is invalid")
    void shouldReturnEmptyMovieByInvalidIdWhileUpdating() {
        when(idHandler.isValid("invalid")).thenReturn(false);

        var actualMovie = underTest.update("invalid", Movie.builder().build());

        assertThat(actualMovie).isEqualTo(createMovie("invalid", null, null, null, null));
    }
}