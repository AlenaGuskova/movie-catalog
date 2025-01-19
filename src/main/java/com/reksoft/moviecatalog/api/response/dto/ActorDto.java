package com.reksoft.moviecatalog.api.response.dto;

import com.reksoft.moviecatalog.domain.model.Actor;

/**
 * DTO for {@link Actor}.
 */
public record ActorDto(String id, String firstName, String lastName) {
}
