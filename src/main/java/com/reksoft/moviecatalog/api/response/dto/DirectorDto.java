package com.reksoft.moviecatalog.api.response.dto;

import com.reksoft.moviecatalog.domain.model.Director;

/**
 * DTO for {@link Director}.
 */
public record DirectorDto(String id, String firstName, String lastName) {
}
