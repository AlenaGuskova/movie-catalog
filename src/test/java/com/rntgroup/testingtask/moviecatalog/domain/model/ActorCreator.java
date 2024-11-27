package com.rntgroup.testingtask.moviecatalog.domain.model;

import com.rntgroup.testingtask.moviecatalog.api.response.dto.Actor;

public class ActorCreator {

    public static ActorEntity createActorEntity(String id, String firstName,
                                                String lastName) {
        return new ActorEntity()
                .setId(id)
                .setFirstName(firstName)
                .setLastName(lastName);
    }

    public static Actor createActor(String id, String firstName,
                                    String lastName) {
        return Actor.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }
}