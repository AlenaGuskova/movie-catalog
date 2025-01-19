package com.reksoft.moviecatalog.domain.repository;

import com.reksoft.moviecatalog.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.reksoft.moviecatalog.domain.model.DirectorCreator.createDirector;
import static org.assertj.core.api.Assertions.assertThat;

class DirectorRepositoryIntegrationTest extends IntegrationTest {

    @Autowired
    private DirectorRepository underTest;

    @Test
    @DisplayName("Should find director by id")
    void shouldFindDirectorById() {
        var actual = underTest.findById("5483a2d9-6fa8-4ab9-b82a-cd032094dd12");

        var expected = createDirector("5483a2d9-6fa8-4ab9-b82a-cd032094dd12", "Питер", "Джексон");
        assertThat(actual).isEqualTo(expected);
    }
}