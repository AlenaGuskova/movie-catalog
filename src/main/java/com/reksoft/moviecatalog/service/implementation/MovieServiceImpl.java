package com.reksoft.moviecatalog.service.implementation;

import java.util.Collection;
import java.util.Optional;

import com.reksoft.moviecatalog.api.request.CreateMovieRequest;
import com.reksoft.moviecatalog.api.request.UpdateMovieRequest;
import com.reksoft.moviecatalog.api.response.dto.MovieDto;
import com.reksoft.moviecatalog.domain.mapper.MovieMapper;
import com.reksoft.moviecatalog.domain.repository.DirectorRepository;
import com.reksoft.moviecatalog.domain.repository.MovieRepository;
import com.reksoft.moviecatalog.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@inheritDoc}
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;
    private final MovieMapper movieMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<MovieDto> getMovies() {
        return movieRepository.findAll()
                .stream()
                .map(movieMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<MovieDto> getByGenre(String genre) {
        return movieRepository.findByGenreIgnoreCase(genre)
                .stream()
                .map(movieMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<MovieDto> getByName(String name) {
        return movieRepository.findByNameIgnoreCase(name)
                .stream()
                .map(movieMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<MovieDto> getByPrefixInTitle(String prefix) {
        return movieRepository.findByPrefixInTitleIgnoreCase(prefix)
                .stream()
                .map(movieMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MovieDto getById(String id) {
        return movieMapper.toDto(movieRepository.findById(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public int delete(String id) {
        return movieRepository.delete(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public MovieDto create(CreateMovieRequest request) {
        var movie = request.movie();
        var director = Optional.ofNullable(movie.directorId())
                .map(directorRepository::findById)
                .orElse(null);
        var mappedMovie = movieMapper.toResource(movie).setDirector(director);
        return movieMapper.toDto(movieRepository.save(mappedMovie));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public MovieDto update(UpdateMovieRequest request) {
        var movie = request.movie();
        var director = Optional.ofNullable(movie.directorId())
                .map(directorRepository::findById)
                .orElse(null);
        var mappedMovie = movieMapper.toResource(movie).setDirector(director);
        return movieMapper.toDto(movieRepository.update(mappedMovie));
    }
}
