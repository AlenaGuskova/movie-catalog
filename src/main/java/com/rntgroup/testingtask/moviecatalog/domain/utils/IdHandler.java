package com.rntgroup.testingtask.moviecatalog.domain.utils;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class IdHandler {

    public String generateId() {
        return UUID.randomUUID().toString();
    }

    public void isValid(String id) {
        Try.of(() -> UUID.fromString(id))
                .onFailure(_ -> log.warn("Invalid resource id: {}", id))
                .getOrElseThrow(_ ->
                        new IllegalArgumentException("Invalid resource id: " + id)
                );
    }
}
