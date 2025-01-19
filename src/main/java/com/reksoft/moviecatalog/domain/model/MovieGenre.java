package com.reksoft.moviecatalog.domain.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class MovieGenre {

    private UUID movieId;
    private UUID genreId;
}
