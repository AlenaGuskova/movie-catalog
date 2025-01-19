package com.reksoft.moviecatalog.domain.repository;

import java.util.List;

import com.reksoft.moviecatalog.IntegrationTest;
import com.reksoft.moviecatalog.domain.exception.ResourceNotFoundException;
import com.reksoft.moviecatalog.domain.model.Movie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static com.reksoft.moviecatalog.domain.model.DirectorCreator.createDirector;
import static com.reksoft.moviecatalog.domain.model.MovieCreator.createMovie;
import static com.reksoft.moviecatalog.domain.model.MovieCreator.getMoviesFromDB;
import static com.reksoft.moviecatalog.domain.model.MovieCreator.getPopularMovie;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MovieRepositoryIntegrationTest extends IntegrationTest {

    @Autowired
    private MovieRepository underTest;

    @Test
    @DisplayName("Should find all movies")
    void shouldFindAllMovies() {
        var actual = underTest.findAll();

        assertThat(actual)
            .isEqualTo(getMoviesFromDB());
    }

    @Test
    @DisplayName("Should not find movies by absent genre")
    void shouldNotFindMoviesByAbsentGenre() {
        var actual = underTest.findByGenreIgnoreCase("absent");

        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("Should find movies by genre ignore case")
    void shouldFindMoviesByGenreIgnoreCase() {
        var actual = underTest.findByGenreIgnoreCase("драма");

        assertThat(actual)
            .contains(getMoviesFromDB().get(0), getMoviesFromDB().get(1));
    }

    @Test
    @DisplayName("Should not find movies by absent fieldName")
    void shouldNotFindMoviesByAbsentName() {
        var actual = underTest.findByNameIgnoreCase("absent");

        assertThat(actual).isEmpty();
    }

    @ParameterizedTest(name = "for name: {0}")
    @ValueSource(strings = {"оливье", "омар", "франсуа", "си"})
    @DisplayName("Should find movies by director's last fieldName ignore case")
    void shouldFindMoviesByName(String name) {
        var actual = underTest.findByNameIgnoreCase(name);

        assertThat(actual)
            .isEqualTo(List.of(getMoviesFromDB().get(0)));
    }

    @ParameterizedTest(name = "for prefix: {0}")
    @ValueSource(strings = {"Вл", "коле", "короля"})
    @DisplayName("Should find movies by prefix in start of movie title ignore case")
    void shouldFindMoviesByPrefixInTitle(String prefix) {
        var actual = underTest.findByPrefixInTitleIgnoreCase(prefix);

        assertThat(actual)
            .isEqualTo(List.of(getPopularMovie()));
    }

    @Test
    @DisplayName("Should not find movies by absent prefix")
    void shouldNotFindMoviesByAbsentPrefix() {
        var actual = underTest.findByPrefixInTitleIgnoreCase("absent");

        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("Should find movie by id")
    void shouldFindMovieById() {
        var actual = underTest.findById("2d21855e-cd44-48b5-9f38-b3d5786b52bd");

        assertThat(actual).isEqualTo(getPopularMovie());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when finding movie by id")
    void shouldThrowResourceNotFoundExceptionWhenFindingMovieById() {
        assertThatThrownBy(() -> underTest.findById("18ffa73f-5ee9-491c-b8f3-36664485f4b1"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Should delete movie")
    void shouldDelete() {
        assertThat(underTest.delete("d890bbaf-f8e7-4c47-861b-e9368fabbd02"))
            .isEqualTo(1);
    }

    @Test
    @DisplayName("Should save movie with director")
    void shouldSaveMovieWithDirector() {
        var movie = createMovie("dd809ec9-e72e-42a3-aac6-95fe4a36b4ff", "Новый мир",
            createDirector("544e0aef-cb6f-413e-8e56-667c2f739e4d"), null, null);
        var actual = underTest.save(movie);

        var expected = createMovie("dd809ec9-e72e-42a3-aac6-95fe4a36b4ff", "Новый мир",
            createDirector("544e0aef-cb6f-413e-8e56-667c2f739e4d", "Оливье", "Накаш"),
            null, null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should save movie")
    void shouldSaveMovie() {
        var movie = createMovie("dd809ec9-e72e-42a3-aac6-95fe4a36b4ff", "Новый мир");
        var actual = underTest.save(movie);

        var expected = createMovie("dd809ec9-e72e-42a3-aac6-95fe4a36b4ff", "Новый мир");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should update movie")
    void shouldUpdateMovie() {
        var actual = underTest.update(createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Новый Властелин колец"));

        assertThat(actual)
            .extracting(Movie::getDirector, Movie::getTitle)
            .contains(null, "Новый Властелин колец");
    }

    @Test
    @DisplayName("Should update movie with director")
    void shouldUpdateMovieWithDirector() {
        var movie = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Новый Властелин колец",
            createDirector("1a3ea417-14b5-4525-bac8-58f7dfd613a6"), null, null);
        var actual = underTest.update(movie);

        assertThat(actual)
            .extracting(Movie::getDirector, Movie::getTitle)
            .contains(createDirector("1a3ea417-14b5-4525-bac8-58f7dfd613a6", "Хаяо", "Миядзаки"),
                "Новый Властелин колец");
    }
}