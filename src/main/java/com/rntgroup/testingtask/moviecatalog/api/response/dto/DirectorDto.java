package com.rntgroup.testingtask.moviecatalog.api.response.dto;

import com.rntgroup.testingtask.moviecatalog.domain.model.Director;
import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link Director}.
 */
@Value
@Builder(toBuilder = true)
public class DirectorDto {
    String id;
    String firstName;
    String lastName;
}
