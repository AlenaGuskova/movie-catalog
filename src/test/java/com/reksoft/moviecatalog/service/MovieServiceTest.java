package com.reksoft.moviecatalog.service;

import java.util.List;

import com.reksoft.moviecatalog.api.request.CreateMovieRequest;
import com.reksoft.moviecatalog.api.request.UpdateMovieRequest;
import com.reksoft.moviecatalog.domain.mapper.MovieMapper;
import com.reksoft.moviecatalog.domain.mapper.MovieMapperImpl;
import com.reksoft.moviecatalog.domain.repository.DirectorRepository;
import com.reksoft.moviecatalog.domain.repository.MovieRepository;
import com.reksoft.moviecatalog.service.implementation.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.reksoft.moviecatalog.domain.model.DirectorCreator.createDirector;
import static com.reksoft.moviecatalog.domain.model.DirectorCreator.createDirectorDto;
import static com.reksoft.moviecatalog.domain.model.MovieCreator.createMovie;
import static com.reksoft.moviecatalog.domain.model.MovieCreator.createMovieDto;
import static com.reksoft.moviecatalog.domain.model.MovieCreator.getPopularMovie;
import static com.reksoft.moviecatalog.domain.model.MovieCreator.getPopularMovieDto;
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
    @DisplayName("Should get all movies")
    void shouldGetMovies() {
        var movie1 = createMovie("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "1+1");
        var movie2 = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Братство Кольца");
        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));

        var actualMovies = underTest.getMovies();

        var expectedMovie1 = createMovieDto("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "1+1");
        var expectedMovie2 = createMovieDto("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Братство Кольца");
        assertThat(actualMovies).hasSize(2)
            .containsExactly(expectedMovie1, expectedMovie2);
    }

    @Test
    @DisplayName("Should get movies by genre")
    void shouldGetMoviesByGenre() {
        var movie1 = createMovie("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "1+1");
        var movie2 = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Братство Кольца");
        when(movieRepository.findByGenreIgnoreCase("драма"))
            .thenReturn(List.of(movie1, movie2));

        var actualMovies = underTest.getByGenre("драма");

        var expectedMovie1 = createMovieDto("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "1+1");
        var expectedMovie2 = createMovieDto("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Братство Кольца");
        assertThat(actualMovies).hasSize(2)
            .containsExactly(expectedMovie1, expectedMovie2);
    }

    @Test
    @DisplayName("Should get movies by name")
    void shouldGetMoviesByName() {
        var movie = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Возвращение короля");
        when(movieRepository.findByNameIgnoreCase("джексон"))
            .thenReturn(List.of(movie));

        var actualMovies = underTest.getByName("джексон");

        assertThat(actualMovies)
            .hasSize(1)
            .containsExactly(createMovieDto("2d21855e-cd44-48b5-9f38-b3d5786b52bd",
                "Властелин колец: Возвращение короля"));
    }

    @Test
    @DisplayName("Should get movies by prefix in movie title")
    void shouldGetMoviesByPrefixInTitle() {
        var movie = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Возвращение короля");
        when(movieRepository.findByPrefixInTitleIgnoreCase("колец"))
            .thenReturn(List.of(movie));

        var actualMovies = underTest.getByPrefixInTitle("колец");

        assertThat(actualMovies)
            .hasSize(1)
            .containsExactly(createMovieDto("2d21855e-cd44-48b5-9f38-b3d5786b52bd",
                "Властелин колец: Возвращение короля"));
    }

    @Test
    @DisplayName("Should get movie by id")
    void shouldGetMovieById() {
        var movie = getPopularMovie();
        when(movieRepository.findById("2d21855e-cd44-48b5-9f38-b3d5786b52bd")).thenReturn(movie);

        var actualMovie = underTest.getById("2d21855e-cd44-48b5-9f38-b3d5786b52bd");

        assertThat(actualMovie).isEqualTo(getPopularMovieDto());
    }

    @Test
    @DisplayName("Should delete movie")
    void shouldDeleteMovieById() {
        when(movieRepository.delete("2d21855e-cd44-48b5-9f38-b3d5786b52bd")).thenReturn(1);

        assertThat(underTest.delete("2d21855e-cd44-48b5-9f38-b3d5786b52bd")).isEqualTo(1);
    }

    @Test
    @DisplayName("Should update movie")
    void shouldUpdateMovieById() {
        var movie = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Новый Властелин колец",
            createDirector("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"), null, null);
        when(movieRepository.update(any())).thenReturn(movie);

        var requestToUpdateMovie = new UpdateMovieRequest(
            new UpdateMovieRequest.MovieRequest("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Новый Властелин колец", "5483a2d9-6fa8-4ab9-b82a-cd032094dd12")
        );
        var actualMovie = underTest.update(requestToUpdateMovie);

        assertThat(actualMovie).isEqualTo(
            createMovieDto("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Новый Властелин колец",
                createDirectorDto("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"))
        );
    }

    @Test
    @DisplayName("Should save movie")
    void shouldSaveMovie() {
        var movie = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Новый Властелин колец",
            createDirector("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"), null, null);
        when(movieRepository.save(any())).thenReturn(movie);

        var requestToCreateMovie = new CreateMovieRequest(
            new CreateMovieRequest.MovieRequest("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Новый Властелин колец", "5483a2d9-6fa8-4ab9-b82a-cd032094dd12")
        );
        var actualMovie = underTest.create(requestToCreateMovie);

        assertThat(actualMovie).isEqualTo(
            createMovieDto("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Новый Властелин колец",
                createDirectorDto("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"))
        );
    }
}