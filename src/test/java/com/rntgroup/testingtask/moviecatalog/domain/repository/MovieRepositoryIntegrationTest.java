package com.rntgroup.testingtask.moviecatalog.domain.repository;

import com.rntgroup.testingtask.moviecatalog.IntegrationTest;
import com.rntgroup.testingtask.moviecatalog.domain.exception.ResourceNotFoundException;
import com.rntgroup.testingtask.moviecatalog.domain.model.Movie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.ResultSet;
import java.util.List;

import static com.rntgroup.testingtask.moviecatalog.domain.model.ActorCreator.createActor;
import static com.rntgroup.testingtask.moviecatalog.domain.model.DirectorCreator.createDirector;
import static com.rntgroup.testingtask.moviecatalog.domain.model.GenreCreator.createGenre;
import static com.rntgroup.testingtask.moviecatalog.domain.model.MovieCreator.createMovie;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MovieRepositoryIntegrationTest extends IntegrationTest {

    @Autowired
    private MovieRepository underTest;

    @Test
    @DisplayName("Should find all movies")
    void shouldFindAllMovies() {
        var actual = underTest.findAll();

        var expected1 = createMovie("d890bbaf-f8e7-4c47-861b-e9368fabbd02", "1+1");
        var expected2 = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Возвращение короля");
        var expected3 = createMovie("e74318b9-0b1b-41c9-8c89-9779ce0261f3", "Унесённые призраками");
        assertThat(actual).hasSize(3)
                .containsExactly(expected1, expected2, expected3);
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

        var expected1 = createMovie("d890bbaf-f8e7-4c47-861b-e9368fabbd02",
                "1+1");
        var expected2 = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd",
                "Властелин колец: Возвращение короля");
        assertThat(actual).hasSize(2)
                .contains(expected1, expected2);
    }

    @Test
    @DisplayName("Should find movies by actor's first name ignore case")
    void shouldReturnListOfMoviesByActorFirstName() {
        var actual = underTest.findByNameIgnoreCase("элайджа");

        assertThat(actual).hasSize(1)
                .contains(createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd",
                        "Властелин колец: Возвращение короля",
                        null, null, null));
    }

    @Test
    @DisplayName("Should not find movies by absent name")
    void shouldNotFindMoviesByAbsentName() {
        var actual = underTest.findByNameIgnoreCase("absent");

        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("Should find movies by actor's last name ignore case")
    void shouldReturnListOfMoviesByActorLastName() {
        var actual = underTest.findByNameIgnoreCase("си");

        assertThat(actual).hasSize(1)
                .contains(createMovie("d890bbaf-f8e7-4c47-861b-e9368fabbd02", "1+1"));
    }

    @Test
    @DisplayName("Should find movies by director's first name ignore case")
    void shouldFindMoviesByDirectorFirstName() {
        var actual = underTest.findByNameIgnoreCase("оливье");

        assertThat(actual).hasSize(1)
                .contains(createMovie("d890bbaf-f8e7-4c47-861b-e9368fabbd02", "1+1"));
    }

    @Test
    @DisplayName("Should find movies by director's last name ignore case")
    void shouldFindMoviesByDirectorLastName() {
        var actual = underTest.findByNameIgnoreCase("джексон");

        assertThat(actual).hasSize(1)
                .contains(createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd",
                        "Властелин колец: Возвращение короля"));
    }

    @Test
    @DisplayName("Should find movies by prefix in middle of movie title ignore case")
    void shouldFindMoviesByPrefixInMiddleOfTitle() {
        var actual = underTest.findByPrefixInTitleIgnoreCase("коле");

        assertThat(actual).hasSize(1)
                .contains(createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd",
                        "Властелин колец: Возвращение короля"));
    }

    @Test
    @DisplayName("Should find movies by prefix in start of movie title ignore case")
    void shouldFindMoviesByPrefixInStartOfTitle() {
        var actual = underTest.findByPrefixInTitleIgnoreCase("1");

        assertThat(actual).hasSize(1)
                .contains(createMovie("d890bbaf-f8e7-4c47-861b-e9368fabbd02", "1+1"));
    }

    @Test
    @DisplayName("Should find movies by prefix in end of movie title ignore case")
    void shouldFindMoviesByPrefixInEndOfTitle() {
        var actual = underTest.findByPrefixInTitleIgnoreCase("короля");

        assertThat(actual).hasSize(1)
                .contains(createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd",
                        "Властелин колец: Возвращение короля"));
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

        var expected = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Возвращение короля",
                createDirector("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"),
                List.of(createGenre("80bd39cc-e349-48fb-9c06-373fe9d04768", "фэнтези"),
                        createGenre("27a33184-9500-4847-b83b-647de0538b2b", "приключения"),
                        createGenre("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                List.of(createActor("3f825e80-f070-4829-85ce-62964a02854a", "Элайджа", "Вуд"),
                        createActor("4a42c006-c853-4795-b98e-3eefe6552940", "Вигго", "Мортенсен"))
        );

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when finding movie by id")
    void shouldThrowResourceNotFoundExceptionWhenFindingMovieById() {
        assertThatThrownBy(() -> underTest.findById("non-existing"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Could not find the resource by identifier: non-existing");
    }

    @Test
    @DisplayName("Should delete movie")
    void shouldDelete() {
        underTest.delete("d890bbaf-f8e7-4c47-861b-e9368fabbd02");

        var query = "SELECT id FROM movie " +
                "WHERE id = 'd890bbaf-f8e7-4c47-861b-e9368fabbd02'";
        assertThatThrownBy(() -> operations.queryForObject(query, ResultSet::getRowId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("Should save movie with director")
    void shouldSaveMovieWithDirector() {
        var toCreate = new Movie()
                .setId("EY4F67ZHY2V3KPJGGGRMIG7X5ON4TMRMQ")
                .setTitle("Новый мир")
                .setDirector(createDirector("544e0aef-cb6f-413e-8e56-667c2f739e4d"));
        var actual = underTest.save(toCreate);

        var expected = createMovie("EY4F67ZHY2V3KPJGGGRMIG7X5ON4TMRMQ", "Новый мир",
                createDirector("544e0aef-cb6f-413e-8e56-667c2f739e4d", "Оливье", "Накаш"),
                null, null);
        assertThat(actual).isEqualTo(expected);
        assertThat(operations.query("SELECT id FROM movie",
                (rs, _) -> new Movie().setId(rs.getString("id"))))
                .hasSize(4);
    }

    @Test
    @DisplayName("Should save movie")
    void shouldSaveMovie() {
        var toCreate = new Movie()
                .setId("EY4F67ZHY2V3KPJGGGRMIG7X5ON4TMRMQ")
                .setTitle("Новый мир");
        var actual = underTest.save(toCreate);

        var expected = createMovie("EY4F67ZHY2V3KPJGGGRMIG7X5ON4TMRMQ", "Новый мир");
        assertThat(actual).isEqualTo(expected);
        assertThat(operations.query("SELECT id FROM movie",
                (rs, _) -> new Movie().setId(rs.getString("id"))))
                .hasSize(4);
    }

    @Test
    @DisplayName("Should update movie")
    void shouldUpdateMovie() {
        var toUpdate = new Movie()
                .setId("2d21855e-cd44-48b5-9f38-b3d5786b52bd")
                .setTitle("Новый Властелин колец");
        var actual = underTest.update(toUpdate);

        var expected = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Новый Властелин колец",
                createDirector("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"),
                List.of(createGenre("80bd39cc-e349-48fb-9c06-373fe9d04768", "фэнтези"),
                        createGenre("27a33184-9500-4847-b83b-647de0538b2b", "приключения"),
                        createGenre("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                List.of(createActor("3f825e80-f070-4829-85ce-62964a02854a", "Элайджа", "Вуд"),
                        createActor("4a42c006-c853-4795-b98e-3eefe6552940", "Вигго", "Мортенсен")));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should update movie with director")
    void shouldUpdateMovieWithDirector() {
        var toUpdate = new Movie()
                .setId("2d21855e-cd44-48b5-9f38-b3d5786b52bd")
                .setTitle("Новый Властелин колец")
                .setDirector(createDirector("1a3ea417-14b5-4525-bac8-58f7dfd613a6"));
        var actual = underTest.update(toUpdate);

        var expected = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Новый Властелин колец",
                createDirector("1a3ea417-14b5-4525-bac8-58f7dfd613a6", "Хаяо", "Миядзаки"),
                List.of(createGenre("80bd39cc-e349-48fb-9c06-373fe9d04768", "фэнтези"),
                        createGenre("27a33184-9500-4847-b83b-647de0538b2b", "приключения"),
                        createGenre("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                List.of(createActor("3f825e80-f070-4829-85ce-62964a02854a", "Элайджа", "Вуд"),
                        createActor("4a42c006-c853-4795-b98e-3eefe6552940", "Вигго", "Мортенсен")));
        assertThat(actual).isEqualTo(expected);
    }
}