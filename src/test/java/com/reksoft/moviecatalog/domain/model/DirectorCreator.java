package com.reksoft.moviecatalog.domain.model;

import java.util.UUID;
import com.reksoft.moviecatalog.api.response.dto.DirectorDto;
import com.reksoft.moviecatalog.domain.model.Director;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DirectorCreator {

    public static Director createDirector(String id, String firstName,
                                          String lastName) {
        return new Director()
                .setId(UUID.fromString(id))
                .setFirstName(firstName)
                .setLastName(lastName);
    }

    public static Director createDirector(String id) {
        return createDirector(id, null, null);
    }

    public static DirectorDto createDirectorDto(String id, String firstName,
                                                String lastName) {
        return new DirectorDto(id, firstName, lastName);
    }

    public static DirectorDto createDirectorDto(String id) {
        return createDirectorDto(id, null, null);
    }
}