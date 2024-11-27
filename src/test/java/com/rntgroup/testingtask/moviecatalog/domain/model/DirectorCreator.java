package com.rntgroup.testingtask.moviecatalog.domain.model;

import com.rntgroup.testingtask.moviecatalog.api.response.dto.Director;

public class DirectorCreator {

    public static DirectorEntity createDirectorEntity(String id, String firstName,
                                                      String lastName) {
        return new DirectorEntity()
                .setId(id)
                .setFirstName(firstName)
                .setLastName(lastName);
    }

    public static DirectorEntity createDirectorEntity(String id) {
        return new DirectorEntity().setId(id);
    }

    public static Director createDirector(String id, String firstName,
                                          String lastName) {
        return Director.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

    public static Director createDirector(String id) {
        return Director.builder().id(id).build();
    }
}