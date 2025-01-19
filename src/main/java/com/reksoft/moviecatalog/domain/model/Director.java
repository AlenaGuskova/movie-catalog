package com.reksoft.moviecatalog.domain.model;

import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Director {

    private UUID id;
    private String firstName;
    private String lastName;
}
