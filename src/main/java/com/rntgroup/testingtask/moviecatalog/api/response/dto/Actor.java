package com.rntgroup.testingtask.moviecatalog.api.response.dto;

import com.rntgroup.testingtask.moviecatalog.domain.model.ActorEntity;
import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link ActorEntity}.
 */
@Value
@Builder(toBuilder = true)
public class Actor {
    String id;
    String firstName;
    String lastName;
}
