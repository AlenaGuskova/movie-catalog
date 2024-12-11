package com.rntgroup.testingtask.moviecatalog.api.response.dto;

import com.rntgroup.testingtask.moviecatalog.domain.model.Director;

/**
 * DTO for {@link Director}.
 */
public record DirectorDto(String id, String firstName, String lastName) {
}
