package com.rntgroup.testingtask.moviecatalog.domain.dao;

import com.rntgroup.testingtask.moviecatalog.domain.mapper.MovieEntityMapper;
import com.rntgroup.testingtask.moviecatalog.domain.mapper.MovieEntityMapperImpl;
import com.rntgroup.testingtask.moviecatalog.domain.model.ActorEntity;
import com.rntgroup.testingtask.moviecatalog.domain.model.DirectorEntity;
import com.rntgroup.testingtask.moviecatalog.domain.model.MovieEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.rntgroup.testingtask.moviecatalog.domain.model.MovieCreator.createMovieEntity;

public class FakeMovieDAO implements MovieDAO {

    private final Map<String, MovieEntity> movies = new HashMap<>();
    private final MovieEntityMapper movieEntityMapper = new MovieEntityMapperImpl();

    @Override
    public Collection<MovieEntity> findAll() {
        return movies.values();
    }

    @Override
    public MovieEntity findById(String id) {
        var found = movies.get(id);
        if (found == null) {
            throw new EntityNotException(id);
        }
        return found;
    }

    @Override
    public MovieEntity save(MovieEntity movie) {
        movies.put(movie.getId(), movie);
        return movies.get(movie.getId());
    }

    @Override
    public MovieEntity update(String id, MovieEntity movie) {
        var found = movies.get(id);
        movieEntityMapper.partialUpdate(movie, found);
        movies.put(id, found);
        return movies.get(id);
    }

    @Override
    public int delete(String id) {
        movies.remove(id);
        return 1;
    }

    @Override
    public List<MovieEntity> findByGenre(String genreTitle) {
        return movies.values().stream()
                .filter(movie -> movie.getGenres()
                        .stream()
                        .anyMatch(genre -> Objects.requireNonNullElse(genreTitle, "").equals(genre.getTitle())))
                .map(movie -> createMovieEntity(movie.getId(), movie.getTitle(), null, null, null))
                .toList();
    }

    @Override
    public List<MovieEntity> findByName(String name) {
        String correctName = Objects.requireNonNullElse(name, "");
        var matchActors = movies.values().stream()
                .filter(movie -> movie.getActors()
                        .stream()
                        .anyMatch(actor -> matchActor(actor, correctName)))
                .map(movie -> createMovieEntity(movie.getId(), movie.getTitle(), null, null, null))
                .collect(Collectors.toCollection(ArrayList::new));
        var matchDirector = movies.values().stream()
                .filter(movie -> matchDirector(movie.getDirector(), correctName))
                .map(movie -> createMovieEntity(movie.getId(), movie.getTitle(), null, null, null))
                .toList();
        matchActors.addAll(matchDirector);
        return matchActors.stream()
                .distinct()
                .toList();
    }

    private boolean matchActor(ActorEntity actor, String correctName) {
        return correctName.equals(actor.getFirstName()) || correctName.equals(actor.getLastName());
    }

    private boolean matchDirector(DirectorEntity director, String correctName) {
        return correctName.equals(director.getFirstName()) || correctName.equals(director.getLastName());
    }

    @Override
    public List<MovieEntity> findByPrefixInTitle(String prefix) {
        var correctPrefix = Objects.requireNonNullElse(prefix, "");
        return movies.values().stream()
                .filter(movie -> matchPrefixInTitle(movie.getTitle(), correctPrefix))
                .map(movie -> createMovieEntity(movie.getId(), movie.getTitle(), null, null, null))
                .toList();
    }

    private boolean matchPrefixInTitle(String title, String correctName) {
        return title.toLowerCase(Locale.ROOT)
                .contains(correctName);
    }
}
