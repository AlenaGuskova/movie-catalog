package com.rntgroup.testingtask.moviecatalog.service;

import com.rntgroup.testingtask.moviecatalog.api.response.dto.Director;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.Movie;
import com.rntgroup.testingtask.moviecatalog.domain.dao.DAO;
import com.rntgroup.testingtask.moviecatalog.domain.dao.MovieDAO;
import com.rntgroup.testingtask.moviecatalog.domain.mapper.MovieEntityMapper;
import com.rntgroup.testingtask.moviecatalog.domain.model.DirectorEntity;
import com.rntgroup.testingtask.moviecatalog.domain.utils.IdHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieDAO movieDAO;
    private final DAO<DirectorEntity> directorDAO;
    private final IdHandler idHandler;
    private final MovieEntityMapper movieEntityMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Movie> listMovies() {
        return movieDAO.findAll()
                .stream()
                .map(movieEntityMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Movie> getByGenre(String genre) {
        return movieDAO.findByGenre(Objects.requireNonNullElse(genre, ""))
                .stream()
                .map(movieEntityMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Movie> getByName(String name) {
        return movieDAO.findByName(Objects.requireNonNullElse(name, ""))
                .stream()
                .map(movieEntityMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Movie> getByPrefixInTitle(String prefix) {
        return movieDAO.findByPrefixInTitle(Objects.requireNonNullElse(prefix, ""))
                .stream()
                .map(movieEntityMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie getById(String id) {
        if (isInvalidId(id)) {
            return Movie.builder()
                    .id(id)
                    .build();
        }
        return movieEntityMapper.toDto(movieDAO.findById(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int delete(String id) {
        return isInvalidId(id) ? 0 : movieDAO.delete(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie create(Movie movie) {
        var directorId = movie.getDirector().getId();
        if (isInvalidId(directorId)) {
            return Movie.builder().build();
        }
        var director = directorDAO.findById(directorId);
        var toCreate = movieEntityMapper.toEntity(movie)
                .setDirector(director)
                .setId(idHandler.generateId());
        return movieEntityMapper.toDto(movieDAO.save(toCreate));
    }

    @Override
    public Movie update(String id, Movie movie) {
        var directorId = Optional.ofNullable(movie.getDirector())
                .map(Director::getId)
                .orElse(null);
        if (isInvalidId(id) || isInvalidId(directorId)) {
            return Movie.builder()
                    .id(id)
                    .build();
        }
        var director = directorDAO.findById(directorId);
        var toUpdate = movieEntityMapper.toEntity(movie)
                .setDirector(director);
        return movieEntityMapper.toDto(movieDAO.update(id, toUpdate));
    }

    private boolean isInvalidId(String id) {
        return !idHandler.isValid(id);
    }
}
