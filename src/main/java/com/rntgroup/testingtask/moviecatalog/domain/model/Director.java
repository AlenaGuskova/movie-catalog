package com.rntgroup.testingtask.moviecatalog.domain.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Director {

    private String id;
    private String firstName;
    private String lastName;
}
