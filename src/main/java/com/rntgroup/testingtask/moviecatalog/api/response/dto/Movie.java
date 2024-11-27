package com.rntgroup.testingtask.moviecatalog.api.response.dto;

import com.rntgroup.testingtask.moviecatalog.domain.model.MovieEntity;
import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * DTO for {@link MovieEntity}.
 */
@Value
@Builder(toBuilder = true)
public class Movie {
    String id;
    String title;
    Director director;
    List<Genre> genres;
    List<Actor> actors;
}
