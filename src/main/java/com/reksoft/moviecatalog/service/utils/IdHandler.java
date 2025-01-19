package com.reksoft.moviecatalog.service.utils;

import java.util.UUID;
import com.reksoft.moviecatalog.service.exception.IdValidationException;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public final class IdHandler {

    public String generateId() {
        return UUID.randomUUID().toString();
    }

    public void isValid(String id) {
        Try.of(() -> UUID.fromString(id))
                .onFailure(_ -> log.warn("Invalid resource id:'{}'", id))
                .getOrElseThrow(_ -> new IdValidationException(id));
    }
}
