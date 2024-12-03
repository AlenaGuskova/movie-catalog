package com.rntgroup.testingtask.moviecatalog.api.response.dto;

import com.rntgroup.testingtask.moviecatalog.domain.model.Actor;
import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link Actor}.
 */
@Value
@Builder(toBuilder = true)
public class ActorDto {
    String id;
    String firstName;
    String lastName;
}
