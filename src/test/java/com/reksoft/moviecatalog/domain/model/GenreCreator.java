package com.reksoft.moviecatalog.domain.model;

import java.util.UUID;
import com.reksoft.moviecatalog.api.response.dto.GenreDto;
import com.reksoft.moviecatalog.domain.model.Genre;
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