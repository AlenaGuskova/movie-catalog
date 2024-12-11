package com.rntgroup.testingtask.moviecatalog.domain.model;

import java.util.UUID;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.GenreDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GenreCreator {

    public static Genre createGenre(String id, String title) {
        return new Genre()
                .setId(UUID.fromString(id))
                .setTitle(title);
    }

    public static GenreDto createGenreDto(String id, String title) {
        return new GenreDto(id, title);
    }
}