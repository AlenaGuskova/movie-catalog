package com.rntgroup.testingtask.moviecatalog.domain.model;

import com.rntgroup.testingtask.moviecatalog.api.response.dto.DirectorDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DirectorCreator {

    public static Director createDirector(String id, String firstName,
                                          String lastName) {
        return new Director()
                .setId(id)
                .setFirstName(firstName)
                .setLastName(lastName);
    }

    public static Director createDirector(String id) {
        return new Director().setId(id);
    }

    public static DirectorDto createDirectorDto(String id, String firstName,
                                                String lastName) {
        return DirectorDto.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

    public static DirectorDto createDirectorDto(String id) {
        return DirectorDto.builder().id(id).build();
    }
}