package com.rntgroup.testingtask.moviecatalog.domain.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class MovieEntity {

    public static final String TABLE_NAME = "movie";

    private String id;
    private String title;
    private DirectorEntity director;
    private List<GenreEntity> genres;
    private List<ActorEntity> actors;
}
