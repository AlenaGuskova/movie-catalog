package com.rntgroup.testingtask.moviecatalog.service;

import com.rntgroup.testingtask.moviecatalog.api.request.CreateMovieRequest;
import com.rntgroup.testingtask.moviecatalog.api.request.UpdateMovieRequest;
import com.rntgroup.testingtask.moviecatalog.domain.mapper.MovieMapper;
import com.rntgroup.testingtask.moviecatalog.domain.mapper.MovieMapperImpl;
import com.rntgroup.testingtask.moviecatalog.domain.repository.DirectorRepository;
import com.rntgroup.testingtask.moviecatalog.domain.repository.MovieRepository;
import com.rntgroup.testingtask.moviecatalog.service.implementation.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.rntgroup.testingtask.moviecatalog.domain.model.ActorCreator.createActor;
import static com.rntgroup.testingtask.moviecatalog.domain.model.ActorCreator.createActorDto;
import static com.rntgroup.testingtask.moviecatalog.domain.model.DirectorCreator.createDirector;
import static com.rntgroup.testingtask.moviecatalog.domain.model.DirectorCreator.createDirectorDto;
import static com.rntgroup.testingtask.moviecatalog.domain.model.GenreCreator.createGenre;
import static com.rntgroup.testingtask.moviecatalog.domain.model.GenreCreator.createGenreDto;
import static com.rntgroup.testingtask.moviecatalog.domain.model.MovieCreator.createMovie;
import static com.rntgroup.testingtask.moviecatalog.domain.model.MovieCreator.createMovieDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    private final MovieRepository movieRepository = mock(MovieRepository.class);
    private final DirectorRepository directorRepository = mock(DirectorRepository.class);
    private final MovieMapper movieMapper = new MovieMapperImpl();

    private MovieService underTest;

    @BeforeEach
    void setUp() {
        underTest = new MovieServiceImpl(movieRepository, directorRepository, movieMapper);
    }

    @Test
    @DisplayName("Should list all movies")
    void shouldListMovies() {
        var movie1 = createMovie("1", "1+1");
        var movie2 = createMovie("2", "Властелин колец: Братство Кольца");
        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));

        var actualMovies = underTest.listMovies();

        var expectedMovie1 = createMovieDto("1", "1+1");
        var expectedMovie2 = createMovieDto("2", "Властелин колец: Братство Кольца");
        assertThat(actualMovies).hasSize(2)
                .containsExactly(expectedMovie1, expectedMovie2);
    }

    @Test
    @DisplayName("Should get movies by genre")
    void shouldGetMoviesByGenre() {
        var movie1 = createMovie("1", "1+1");
        var movie2 = createMovie("2", "Властелин колец: Братство Кольца");
        when(movieRepository.findByGenreIgnoreCase("драма"))
                .thenReturn(List.of(movie1, movie2));

        var actualMovies = underTest.getByGenre("драма");

        var expectedMovie1 = createMovieDto("1", "1+1");
        var expectedMovie2 = createMovieDto("2", "Властелин колец: Братство Кольца");
        assertThat(actualMovies).hasSize(2)
                .containsExactly(expectedMovie1, expectedMovie2);
    }

    @Test
    @DisplayName("Should get movies by name")
    void shouldGetMoviesByName() {
        var movie = createMovie("2", "Властелин колец: Возвращение короля");
        when(movieRepository.findByNameIgnoreCase("джексон"))
                .thenReturn(List.of(movie));

        var actualMovies = underTest.getByName("джексон");

        assertThat(actualMovies)
                .hasSize(1)
                .containsExactly(createMovieDto("2", "Властелин колец: Возвращение короля"));
    }

    @Test
    @DisplayName("Should get movies by prefix in movie title")
    void shouldGetMoviesByPrefixInTitle() {
        var movie = createMovie("2", "Властелин колец: Возвращение короля");
        when(movieRepository.findByPrefixInTitleIgnoreCase("колец"))
                .thenReturn(List.of(movie));

        var actualMovies = underTest.getByPrefixInTitle("колец");

        assertThat(actualMovies)
                .hasSize(1)
                .containsExactly(createMovieDto("2", "Властелин колец: Возвращение короля"));
    }

    @Test
    @DisplayName("Should get movie by id")
    void shouldGetMovieById() {
        var movie = createMovie("id", "Властелин колец: Братство Кольца",
                createDirector("1", "Питер", "Джексон"),
                List.of(createGenre("3", "фэнтези"),
                        createGenre("4", "приключения"),
                        createGenre("5", "драма")),
                List.of(createActor("3", "Элайджа", "Вуд"),
                        createActor("4", "Вигго", "Мортенсен"))
        );
        when(movieRepository.findById("id")).thenReturn(movie);

        var actualMovie = underTest.getById("id");

        var expectedMovie = createMovieDto("id", "Властелин колец: Братство Кольца",
                createDirectorDto("1", "Питер", "Джексон"),
                List.of(createGenreDto("3", "фэнтези"),
                        createGenreDto("4", "приключения"),
                        createGenreDto("5", "драма")),
                List.of(createActorDto("3", "Элайджа", "Вуд"),
                        createActorDto("4", "Вигго", "Мортенсен"))
        );
        assertThat(actualMovie).isEqualTo(expectedMovie);
    }

    @Test
    @DisplayName("Should delete movie")
    void shouldDeleteMovieById() {
        when(movieRepository.delete("id")).thenReturn(1);

        assertThat(underTest.delete("id")).isEqualTo(1);
    }

    @Test
    @DisplayName("Should update movie")
    void shouldUpdateMovieById() {
        var movie = createMovie("id", "Новый Властелин колец",
                createDirector("directorId", "Питер", "Джексон"), null, null);
        when(movieRepository.update(any())).thenReturn(movie);

        var toUpdate = new UpdateMovieRequest(
                UpdateMovieRequest.MovieRequest.builder()
                        .id("id")
                        .title("Новый Властелин колец")
                        .directorId("directorId")
                        .build()
        );
        var actualMovie = underTest.update(toUpdate);

        assertThat(actualMovie).isEqualTo(
                createMovieDto("id", "Новый Властелин колец",
                        createDirectorDto("directorId", "Питер", "Джексон"),
                        null, null)
        );
    }

    @Test
    @DisplayName("Should save movie")
    void shouldSaveMovie() {
        var movie = createMovie("id", "Новый Властелин колец",
                createDirector("directorId", "Питер", "Джексон"), null, null);
        when(movieRepository.save(any())).thenReturn(movie);

        var toCreate = new CreateMovieRequest(
                CreateMovieRequest.MovieRequest.builder()
                        .id("id")
                        .title("Новый Властелин колец")
                        .directorId("directorId")
                        .build()
        );
        var actualMovie = underTest.create(toCreate);

        assertThat(actualMovie).isEqualTo(
                createMovieDto("id", "Новый Властелин колец",
                        createDirectorDto("directorId", "Питер", "Джексон"),
                        null, null)
        );
    }
}