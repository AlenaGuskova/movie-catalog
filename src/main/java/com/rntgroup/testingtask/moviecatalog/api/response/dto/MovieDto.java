package com.rntgroup.testingtask.moviecatalog.api.response.dto;

import com.rntgroup.testingtask.moviecatalog.domain.model.Movie;
import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * DTO for {@link Movie}.
 */
@Value
@Builder(toBuilder = true)
public class MovieDto {
    String id;
    String title;
    DirectorDto director;
    List<GenreDto> genres;
    List<ActorDto> actors;
}
