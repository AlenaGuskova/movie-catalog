package com.reksoft.moviecatalog.domain.model;

import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@Data
@Accessors(chain = true)
@FieldNameConstants
public class Movie {

    private UUID id;
    private String title;
    private Director director;
    private List<Genre> genres;
    private List<Actor> actors;
}
