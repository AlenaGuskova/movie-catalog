package com.rntgroup.testingtask.moviecatalog.domain.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GenreEntity {

    public static final String TABLE_NAME = "genre";

    private String id;
    private String title;
}
