package com.rntgroup.testingtask.moviecatalog.domain.model;

import com.rntgroup.testingtask.moviecatalog.api.response.dto.ActorDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ActorCreator {

    public static Actor createActor(String id, String firstName,
                                    String lastName) {
        return new Actor()
                .setId(id)
                .setFirstName(firstName)
                .setLastName(lastName);
    }

    public static ActorDto createActorDto(String id, String firstName,
                                          String lastName) {
        return ActorDto.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }
}