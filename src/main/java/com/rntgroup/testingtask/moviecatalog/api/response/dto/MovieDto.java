package com.rntgroup.testingtask.moviecatalog.api.response.dto;

import java.util.List;
import com.rntgroup.testingtask.moviecatalog.domain.model.Movie;

/**
 * DTO for {@link Movie}.
 */
public record MovieDto(String id, String title, DirectorDto director,
                       List<GenreDto> genres, List<ActorDto> actors) {
}
