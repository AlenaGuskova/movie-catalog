package com.rntgroup.testingtask.moviecatalog.domain.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ActorEntity {

    public static final String TABLE_NAME = "actor";

    private String id;
    private String firstName;
    private String lastName;
}
