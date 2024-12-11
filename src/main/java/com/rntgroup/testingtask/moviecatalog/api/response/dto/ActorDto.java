package com.rntgroup.testingtask.moviecatalog.api.response.dto;

import com.rntgroup.testingtask.moviecatalog.domain.model.Actor;

/**
 * DTO for {@link Actor}.
 */
public record ActorDto(String id, String firstName, String lastName) {
}
