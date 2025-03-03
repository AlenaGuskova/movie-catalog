package com.reksoft.moviecatalog.domain.model;

import java.util.UUID;
import com.reksoft.moviecatalog.api.response.dto.ActorDto;
import com.reksoft.moviecatalog.domain.model.Actor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ActorCreator {

    public static Actor createActor(String id, String firstName,
                                    String lastName) {
        return new Actor()
                .setId(UUID.fromString(id))
                .setFirstName(firstName)
                .setLastName(lastName);
    }

    public static ActorDto createActorDto(String id, String firstName,
                                          String lastName) {
        return new ActorDto(id, firstName, lastName);
    }
}