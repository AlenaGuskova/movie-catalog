package com.reksoft.moviecatalog.domain.model;

import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MovieActor {

    private UUID movieId;
    private UUID actorId;
}
