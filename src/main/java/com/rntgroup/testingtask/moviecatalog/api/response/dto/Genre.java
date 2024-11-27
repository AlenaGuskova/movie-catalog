package com.rntgroup.testingtask.moviecatalog.api.response.dto;

import com.rntgroup.testingtask.moviecatalog.domain.model.GenreEntity;
import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link GenreEntity}.
 */
@Value
@Builder(toBuilder = true)
public class Genre {
    String id;
    String title;
}
