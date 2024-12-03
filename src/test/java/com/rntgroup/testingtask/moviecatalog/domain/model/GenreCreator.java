package com.rntgroup.testingtask.moviecatalog.domain.model;

import com.rntgroup.testingtask.moviecatalog.api.response.dto.GenreDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GenreCreator {

    public static Genre createGenre(String id, String title) {
        return new Genre()
                .setId(id)
                .setTitle(title);
    }

    public static GenreDto createGenreDto(String id, String title) {
        return GenreDto.builder()
                .id(id)
                .title(title)
                .build();
    }
}