package com.rntgroup.testingtask.moviecatalog.domain.model;

import com.rntgroup.testingtask.moviecatalog.api.response.dto.Genre;

public class GenreCreator {

    public static GenreEntity createGenreEntity(String id, String title) {
        return new GenreEntity()
                .setId(id)
                .setTitle(title);
    }

    public static Genre createGenre(String id, String title) {
        return Genre.builder()
                .id(id)
                .title(title)
                .build();
    }
}