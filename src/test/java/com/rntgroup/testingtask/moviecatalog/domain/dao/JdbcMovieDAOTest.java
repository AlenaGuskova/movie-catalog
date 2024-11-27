package com.rntgroup.testingtask.moviecatalog.domain.dao;

import com.rntgroup.testingtask.moviecatalog.BaseIntegrationTest;
import com.rntgroup.testingtask.moviecatalog.domain.model.MovieEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.rntgroup.testingtask.moviecatalog.domain.model.ActorCreator.createActorEntity;
import static com.rntgroup.testingtask.moviecatalog.domain.model.DirectorCreator.createDirectorEntity;
import static com.rntgroup.testingtask.moviecatalog.domain.model.GenreCreator.createGenreEntity;
import static com.rntgroup.testingtask.moviecatalog.domain.model.MovieCreator.createMovieEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql(value = {"/db/scripts/truncate_db.sql",
        "/db/scripts/actors.sql", "/db/scripts/directors.sql",
        "/db/scripts/genres.sql", "/db/scripts/movies.sql",
        "/db/scripts/movie_genres.sql", "/db/scripts/movie_actors.sql"})
@Sql(value = "/db/scripts/truncate_db.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class JdbcMovieDAOTest extends BaseIntegrationTest {

    @Autowired
    private MovieDAO underTest;

    @Test
    @DisplayName("Should return a list of movies successfully")
    void shouldReturnListOfMoviesSuccessfully() {
        var actualMovies = underTest.findAll();

        var expectedMovie1 = createMovieEntity("ANLGF35FKR7H22X7Y4QI75E7ADKGDNGK9", "1+1",
                createDirectorEntity("VAWDZWKY3PS45TC7KYAVRQKSEGSMXKPJ3", "Оливье", "Накаш"),
                List.of(createGenreEntity("3YYHSZMUWWR75DYY2XJIITS4LAS2KSCZP", "драма"),
                        createGenreEntity("CB74INE5AU5KGFNKIO3FGI7XB75N53R2V", "комедия")),
                List.of(createActorEntity("MKZPSTCOCLW3G75LWXIMUINS6S5OK7ZLR", "Франсуа", "Клюзе"),
                        createActorEntity("WPHFPMOFVQPZXFF5CTDI53HKGQORWNUBU", "Омар", "Си"))
        );
        var expectedMovie2 = createMovieEntity("VONQUE7X4WTQ4UQK4GLL723UVHD3MENBN", "Властелин колец: Возвращение короля",
                createDirectorEntity("VUBMYE5Y3DWAV6DPXYWXR7HD6P3T7QXUU", "Питер", "Джексон"),
                List.of(createGenreEntity("MZHPJANCLZTISDBANQHU3DVCZP5EDU3J9", "фэнтези"),
                        createGenreEntity("JLEWEWDQITKWSBB2EQKJDDWU3FNAVGZJC", "приключения"),
                        createGenreEntity("3YYHSZMUWWR75DYY2XJIITS4LAS2KSCZP", "драма")),
                List.of(createActorEntity("ZCZNQLEP5S3KQCKINZN3PE3F7I2GVY3S6", "Элайджа", "Вуд"),
                        createActorEntity("UMVMV5SSVIFUXXHQQVJ4TVTZ74ENFKATR", "Вигго", "Мортенсен"))
        );
        var expectedMovie3 = createMovieEntity("GPSUXXKYX77FXD72HJMDSDV77N5MHXJ3E", "Унесённые призраками",
                createDirectorEntity("IVAX5QJPMSWLYUJ6PK3JFKZB6NSCXZ2SH", "Хаяо", "Миядзаки"),
                List.of(createGenreEntity("RD4IXX5HU2CDTW23FL6Z6ZQEOOWQPMU7D", "аниме")),
                List.of(createActorEntity("NJ2ZJHZ6VGKRMLNTRXNJKM3B5K455HXIF", "Руми", "Хиираги"),
                        createActorEntity("J67TUWVIS6YKLPVXDHVEZVJFTFQJLETRM", "Мию", "Ирино"))
        );

        assertThat(actualMovies).hasSize(3)
                .containsExactly(expectedMovie1, expectedMovie2, expectedMovie3);
    }

    @Test
    @DisplayName("Should return an empty list of movies by non-exiting genre")
    void shouldReturnEmptyListOfMoviesByNonExistingGenre() {
        var actualMovies = underTest.findByGenre("история");

        assertThat(actualMovies).isEmpty();
    }

    @Test
    @DisplayName("Should return a list of movies by genre successfully")
    void shouldReturnListOfMoviesByGenreSuccessfully() {
        var actualMovies = underTest.findByGenre("Драма");

        var expectedMovie1 = createMovieEntity("ANLGF35FKR7H22X7Y4QI75E7ADKGDNGK9", "1+1",
                null, null, null);
        var expectedMovie2 = createMovieEntity("VONQUE7X4WTQ4UQK4GLL723UVHD3MENBN", "Властелин колец: Возвращение короля",
                null, null, null);

        assertThat(actualMovies).hasSize(2)
                .contains(expectedMovie1, expectedMovie2);
    }

    @Test
    @DisplayName("Should return an empty list of movies by non-exiting name")
    void shouldReturnEmptyListOfMoviesByNonExistingName() {
        var actualMovies = underTest.findByGenre("Ян");

        assertThat(actualMovies).isEmpty();
    }

    @Test
    @DisplayName("Should return a list of movies by actor's first name successfully")
    void shouldReturnListOfMoviesByActorFirstName() {
        var actualMovies = underTest.findByName("элайджа");

        assertThat(actualMovies).hasSize(1)
                .contains(createMovieEntity("VONQUE7X4WTQ4UQK4GLL723UVHD3MENBN",
                        "Властелин колец: Возвращение короля",
                        null, null, null));
    }

    @Test
    @DisplayName("Should return a list of movies by actor's last name successfully")
    void shouldReturnListOfMoviesByActorLastName() {
        var actualMovies = underTest.findByName("Си");

        assertThat(actualMovies).hasSize(1)
                .contains(createMovieEntity("ANLGF35FKR7H22X7Y4QI75E7ADKGDNGK9", "1+1",
                        null, null, null));
    }

    @Test
    @DisplayName("Should return a list of movies by director's first name successfully")
    void shouldReturnListOfMoviesByDirectorFirstName() {
        var actualMovies = underTest.findByName(" Оливье");

        assertThat(actualMovies).hasSize(1)
                .contains(createMovieEntity("ANLGF35FKR7H22X7Y4QI75E7ADKGDNGK9", "1+1",
                        null, null, null));
    }

    @Test
    @DisplayName("Should return a list of movies by director's last name successfully")
    void shouldReturnListOfMoviesByDirectorLastName() {
        var actualMovies = underTest.findByName("Джексон                  ");

        assertThat(actualMovies).hasSize(1)
                .contains(
                        createMovieEntity("VONQUE7X4WTQ4UQK4GLL723UVHD3MENBN", "Властелин колец: Возвращение короля",
                                null, null, null)
                );
    }

    @Test
    @DisplayName("Should return a list of movies by prefix in middle of movie title successfully")
    void shouldReturnListOfMoviesByPrefixInMiddleOfTitle() {
        var actualMovies = underTest.findByPrefixInTitle("  коле  ");

        assertThat(actualMovies).hasSize(1)
                .contains(
                        createMovieEntity("VONQUE7X4WTQ4UQK4GLL723UVHD3MENBN", "Властелин колец: Возвращение короля",
                                null, null, null)
                );
    }

    @Test
    @DisplayName("Should return a list of movies by prefix in start of movie title successfully")
    void shouldReturnListOfMoviesByPrefixInStartOfTitle() {
        var actualMovies = underTest.findByPrefixInTitle("1 ");

        assertThat(actualMovies).hasSize(1)
                .contains(
                        createMovieEntity("ANLGF35FKR7H22X7Y4QI75E7ADKGDNGK9", "1+1",
                                null, null, null)
                );
    }

    @Test
    @DisplayName("Should return a list of movies by prefix in end of movie title successfully")
    void shouldReturnListOfMoviesByPrefixInEndOfTitle() {
        var actualMovies = underTest.findByPrefixInTitle("  короля");

        assertThat(actualMovies).hasSize(1)
                .contains(createMovieEntity("VONQUE7X4WTQ4UQK4GLL723UVHD3MENBN", "Властелин колец: Возвращение короля",
                        null, null, null));
    }

    @Test
    @DisplayName("Should return movie by id successfully")
    void shouldReturnMovieById() {
        var actualMovie = underTest.findById("VONQUE7X4WTQ4UQK4GLL723UVHD3MENBN");

        var expectedMovie = createMovieEntity("VONQUE7X4WTQ4UQK4GLL723UVHD3MENBN", "Властелин колец: Возвращение короля",
                createDirectorEntity("VUBMYE5Y3DWAV6DPXYWXR7HD6P3T7QXUU", "Питер", "Джексон"),
                List.of(createGenreEntity("MZHPJANCLZTISDBANQHU3DVCZP5EDU3J9", "фэнтези"),
                        createGenreEntity("JLEWEWDQITKWSBB2EQKJDDWU3FNAVGZJC", "приключения"),
                        createGenreEntity("3YYHSZMUWWR75DYY2XJIITS4LAS2KSCZP", "драма")),
                List.of(createActorEntity("ZCZNQLEP5S3KQCKINZN3PE3F7I2GVY3S6", "Элайджа", "Вуд"),
                        createActorEntity("UMVMV5SSVIFUXXHQQVJ4TVTZ74ENFKATR", "Вигго", "Мортенсен"))
        );

        assertThat(actualMovie).isEqualTo(expectedMovie);
    }

    @Test
    @DisplayName("Should throw EntityNotException when movie has not found by id")
    void shouldThrowExceptionWhenMovieNotFoundById() {
        assertThatThrownBy(() -> underTest.findById("non-existing"))
                .isInstanceOf(EntityNotException.class)
                .hasMessage("Couldn't find entity by id: non-existing");
    }

    @Test
    @DisplayName("Should delete movie by id successfully")
    void shouldDeleteMovieById() {
        underTest.delete("ANLGF35FKR7H22X7Y4QI75E7ADKGDNGK9");

        assertThat(underTest.findAll()).hasSize(2);
        assertThatThrownBy(() -> underTest.findById("ANLGF35FKR7H22X7Y4QI75E7ADKGDNGK9"))
                .isInstanceOf(EntityNotException.class);
    }

    @Test
    @DisplayName("Should save new movie successfully")
    void shouldSaveNewMovieById() {
        var toCreate = new MovieEntity()
                .setId("EY4F67ZHY2V3KPJGGGRMIG7X5ON4TMRMQ")
                .setTitle("Новый мир")
                .setDirector(createDirectorEntity("VAWDZWKY3PS45TC7KYAVRQKSEGSMXKPJ3"));
        var actualMovie = underTest.save(toCreate);

        var expectedMovie = createMovieEntity("EY4F67ZHY2V3KPJGGGRMIG7X5ON4TMRMQ", "Новый мир",
                createDirectorEntity("VAWDZWKY3PS45TC7KYAVRQKSEGSMXKPJ3", "Оливье", "Накаш"),
                List.of(), List.of()
        );
        assertThat(actualMovie).isEqualTo(expectedMovie);
        assertThat(underTest.findAll()).hasSize(4);
    }

    @Test
    @DisplayName("Should update movie by id successfully")
    void shouldUpdateMovieById() {
        assertThat(underTest.findById("ANLGF35FKR7H22X7Y4QI75E7ADKGDNGK9").getTitle())
                .isEqualTo("1+1");

        var toCreate = new MovieEntity()
                .setId("ANLGF35FKR7H22X7Y4QI75E7ADKGDNGK9")
                .setTitle("Новый мир");
        var actualMovie = underTest.update("ANLGF35FKR7H22X7Y4QI75E7ADKGDNGK9", toCreate);

        assertThat(actualMovie.getId())
                .isEqualTo("ANLGF35FKR7H22X7Y4QI75E7ADKGDNGK9");
        assertThat(actualMovie.getTitle())
                .isEqualTo("Новый мир");
    }
}