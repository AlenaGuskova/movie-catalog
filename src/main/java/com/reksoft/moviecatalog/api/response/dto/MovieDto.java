package com.reksoft.moviecatalog.api.response.dto;

import java.util.List;
import com.reksoft.moviecatalog.domain.model.Movie;
import lombok.experimental.FieldNameConstants;

/**
 * DTO for {@link Movie}.
 */
@FieldNameConstants
public record MovieDto(String id, String title, DirectorDto director,
                       List<GenreDto> genres, List<ActorDto> actors) {
}
