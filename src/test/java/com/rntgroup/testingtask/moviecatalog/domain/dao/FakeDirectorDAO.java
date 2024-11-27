package com.rntgroup.testingtask.moviecatalog.domain.dao;

import com.rntgroup.testingtask.moviecatalog.domain.model.DirectorEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FakeDirectorDAO implements DAO<DirectorEntity> {

    private final Map<String, DirectorEntity> directors = new HashMap<>();

    @Override
    public Collection<DirectorEntity> findAll() {
        return directors.values();
    }

    @Override
    public DirectorEntity findById(String id) {
        var found = directors.get(id);
        if (found == null) {
            throw new EntityNotException(id);
        }
        return found;
    }

    @Override
    public DirectorEntity save(DirectorEntity director) {
        directors.put(director.getId(), director);
        return directors.get(director.getId());
    }

    @Override
    public DirectorEntity update(String id, DirectorEntity director) {
        directors.put(id, director);
        return directors.get(director.getId());
    }

    @Override
    public int delete(String id) {
        directors.remove(id);
        return 1;
    }
}
