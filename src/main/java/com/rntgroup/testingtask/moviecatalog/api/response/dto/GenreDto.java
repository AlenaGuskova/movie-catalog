package com.rntgroup.testingtask.moviecatalog.api.response.dto;

import com.rntgroup.testingtask.moviecatalog.domain.model.Genre;
import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link Genre}.
 */
@Value
@Builder(toBuilder = true)
public class GenreDto {
    String id;
    String title;
}
