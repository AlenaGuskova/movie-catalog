package com.rntgroup.testingtask.moviecatalog.api.response.dto;

import com.rntgroup.testingtask.moviecatalog.domain.model.DirectorEntity;
import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link DirectorEntity}.
 */
@Value
@Builder(toBuilder = true)
public class Director {
    String id;
    String firstName;
    String lastName;
}
