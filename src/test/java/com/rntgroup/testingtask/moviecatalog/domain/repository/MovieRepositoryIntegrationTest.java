package com.rntgroup.testingtask.moviecatalog.domain.repository;

import java.util.List;
import com.rntgroup.testingtask.moviecatalog.IntegrationTest;
import com.rntgroup.testingtask.moviecatalog.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

        var expected1 = createMovie("d890bbaf-f8e7-4c47-861b-e9368fabbd02", "1+1",
                createDirector("544e0aef-cb6f-413e-8e56-667c2f739e4d", "Оливье", "Накаш"),
                List.of(createGenre("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                List.of(createActor("64887d99-ff1d-48da-b066-7dbeb0f464f3", "Франсуа", "Клюзе"),
                        createActor("1abf568a-7106-49f1-b152-d002ba78df7d", "Омар", "Си")));
        var expected2 = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Возвращение короля",
                createDirector("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"),
                List.of(createGenre("80bd39cc-e349-48fb-9c06-373fe9d04768", "фэнтези"),
                        createGenre("27a33184-9500-4847-b83b-647de0538b2b", "приключения"),
                        createGenre("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                List.of(createActor("3f825e80-f070-4829-85ce-62964a02854a", "Элайджа", "Вуд"),
                        createActor("4a42c006-c853-4795-b98e-3eefe6552940", "Вигго", "Мортенсен")));
        var expected3 = createMovie("e74318b9-0b1b-41c9-8c89-9779ce0261f3", "Унесённые призраками");
        assertThat(actual).hasSize(3)
                .containsExactlyInAnyOrder(expected1, expected2, expected3);
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

        var expected1 = createMovie("d890bbaf-f8e7-4c47-861b-e9368fabbd02", "1+1",
                createDirector("544e0aef-cb6f-413e-8e56-667c2f739e4d", "Оливье", "Накаш"),
                List.of(createGenre("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                List.of(createActor("64887d99-ff1d-48da-b066-7dbeb0f464f3", "Франсуа", "Клюзе"),
                        createActor("1abf568a-7106-49f1-b152-d002ba78df7d", "Омар", "Си")));
        var expected2 = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Возвращение короля",
                createDirector("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"),
                List.of(createGenre("80bd39cc-e349-48fb-9c06-373fe9d04768", "фэнтези"),
                        createGenre("27a33184-9500-4847-b83b-647de0538b2b", "приключения"),
                        createGenre("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                List.of(createActor("3f825e80-f070-4829-85ce-62964a02854a", "Элайджа", "Вуд"),
                        createActor("4a42c006-c853-4795-b98e-3eefe6552940", "Вигго", "Мортенсен")));
        assertThat(actual).hasSize(2)
                .contains(expected1, expected2);
    }

    @Test
    @DisplayName("Should find movies by actor's first fieldName ignore case")
    void shouldReturnListOfMoviesByActorFirstName() {
        var actual = underTest.findByNameIgnoreCase("элайджа");

        var expected = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Возвращение короля",
                createDirector("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"),
                List.of(createGenre("80bd39cc-e349-48fb-9c06-373fe9d04768", "фэнтези"),
                        createGenre("27a33184-9500-4847-b83b-647de0538b2b", "приключения"),
                        createGenre("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                List.of(createActor("3f825e80-f070-4829-85ce-62964a02854a", "Элайджа", "Вуд"),
                        createActor("4a42c006-c853-4795-b98e-3eefe6552940", "Вигго", "Мортенсен")));
        assertThat(actual).hasSize(1)
                .contains(expected);
    }

    @Test
    @DisplayName("Should not find movies by absent fieldName")
    void shouldNotFindMoviesByAbsentName() {
        var actual = underTest.findByNameIgnoreCase("absent");

        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("Should find movies by actor's last fieldName ignore case")
    void shouldReturnListOfMoviesByActorLastName() {
        var actual = underTest.findByNameIgnoreCase("си");

        var expected = createMovie("d890bbaf-f8e7-4c47-861b-e9368fabbd02", "1+1",
                createDirector("544e0aef-cb6f-413e-8e56-667c2f739e4d", "Оливье", "Накаш"),
                List.of(createGenre("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                List.of(createActor("64887d99-ff1d-48da-b066-7dbeb0f464f3", "Франсуа", "Клюзе"),
                        createActor("1abf568a-7106-49f1-b152-d002ba78df7d", "Омар", "Си")));
        assertThat(actual).hasSize(1)
                .contains(expected);
    }

    @Test
    @DisplayName("Should find movies by director's first fieldName ignore case")
    void shouldFindMoviesByDirectorFirstName() {
        var actual = underTest.findByNameIgnoreCase("оливье");

        var expected = createMovie("d890bbaf-f8e7-4c47-861b-e9368fabbd02", "1+1",
                createDirector("544e0aef-cb6f-413e-8e56-667c2f739e4d", "Оливье", "Накаш"),
                List.of(createGenre("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                List.of(createActor("64887d99-ff1d-48da-b066-7dbeb0f464f3", "Франсуа", "Клюзе"),
                        createActor("1abf568a-7106-49f1-b152-d002ba78df7d", "Омар", "Си")));
        assertThat(actual).hasSize(1)
                .contains(expected);
    }

    @Test
    @DisplayName("Should find movies by director's last fieldName ignore case")
    void shouldFindMoviesByDirectorLastName() {
        var actual = underTest.findByNameIgnoreCase("джексон");

        var expected = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Возвращение короля",
                createDirector("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"),
                List.of(createGenre("80bd39cc-e349-48fb-9c06-373fe9d04768", "фэнтези"),
                        createGenre("27a33184-9500-4847-b83b-647de0538b2b", "приключения"),
                        createGenre("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                List.of(createActor("3f825e80-f070-4829-85ce-62964a02854a", "Элайджа", "Вуд"),
                        createActor("4a42c006-c853-4795-b98e-3eefe6552940", "Вигго", "Мортенсен")));
        assertThat(actual).hasSize(1)
                .contains(expected);
    }

    @Test
    @DisplayName("Should find movies by prefix in middle of movie title ignore case")
    void shouldFindMoviesByPrefixInMiddleOfTitle() {
        var actual = underTest.findByPrefixInTitleIgnoreCase("коле");

        var expected = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Возвращение короля",
                createDirector("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"),
                List.of(createGenre("80bd39cc-e349-48fb-9c06-373fe9d04768", "фэнтези"),
                        createGenre("27a33184-9500-4847-b83b-647de0538b2b", "приключения"),
                        createGenre("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                List.of(createActor("3f825e80-f070-4829-85ce-62964a02854a", "Элайджа", "Вуд"),
                        createActor("4a42c006-c853-4795-b98e-3eefe6552940", "Вигго", "Мортенсен")));
        assertThat(actual).hasSize(1)
                .contains(expected);
    }

    @Test
    @DisplayName("Should find movies by prefix in start of movie title ignore case")
    void shouldFindMoviesByPrefixInStartOfTitle() {
        var actual = underTest.findByPrefixInTitleIgnoreCase("1");

        var expected = createMovie("d890bbaf-f8e7-4c47-861b-e9368fabbd02", "1+1",
                createDirector("544e0aef-cb6f-413e-8e56-667c2f739e4d", "Оливье", "Накаш"),
                List.of(createGenre("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                List.of(createActor("64887d99-ff1d-48da-b066-7dbeb0f464f3", "Франсуа", "Клюзе"),
                        createActor("1abf568a-7106-49f1-b152-d002ba78df7d", "Омар", "Си")));
        assertThat(actual).hasSize(1)
                .contains(expected);
    }

    @Test
    @DisplayName("Should find movies by prefix in end of movie title ignore case")
    void shouldFindMoviesByPrefixInEndOfTitle() {
        var actual = underTest.findByPrefixInTitleIgnoreCase("короля");

        var expected = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Властелин колец: Возвращение короля",
                createDirector("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон"),
                List.of(createGenre("80bd39cc-e349-48fb-9c06-373fe9d04768", "фэнтези"),
                        createGenre("27a33184-9500-4847-b83b-647de0538b2b", "приключения"),
                        createGenre("a05830f8-3af8-4f9a-bb45-a88688b9edce", "драма")),
                List.of(createActor("3f825e80-f070-4829-85ce-62964a02854a", "Элайджа", "Вуд"),
                        createActor("4a42c006-c853-4795-b98e-3eefe6552940", "Вигго", "Мортенсен")));
        assertThat(actual).hasSize(1)
                .contains(expected);
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
        var movie = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Новый Властелин колец");
        var actual = underTest.update(movie);

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
        var movie = createMovie("2d21855e-cd44-48b5-9f38-b3d5786b52bd", "Новый Властелин колец",
                createDirector("1a3ea417-14b5-4525-bac8-58f7dfd613a6"), null, null);
        var actual = underTest.update(movie);

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