package com.rntgroup.testingtask.moviecatalog.domain.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Movie {

    private String id;
    private String title;
    private Director director;
    private List<Genre> genres;
    private List<Actor> actors;
}
