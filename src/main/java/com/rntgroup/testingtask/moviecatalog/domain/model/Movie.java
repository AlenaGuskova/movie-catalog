package com.rntgroup.testingtask.moviecatalog.domain.model;

import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Movie {

    private UUID id;
    private String title;
    private Director director;
    private List<Genre> genres;
    private List<Actor> actors;
}
