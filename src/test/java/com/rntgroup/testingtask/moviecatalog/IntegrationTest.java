package com.rntgroup.testingtask.moviecatalog;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(value = {"/db/scripts/truncate_db.sql",
        "/db/scripts/actors.sql", "/db/scripts/directors.sql",
        "/db/scripts/genres.sql", "/db/scripts/movies.sql",
        "/db/scripts/movie_genres.sql", "/db/scripts/movie_actors.sql"})
@Sql(value = "/db/scripts/truncate_db.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class IntegrationTest {
}