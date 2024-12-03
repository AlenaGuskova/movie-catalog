package com.rntgroup.testingtask.moviecatalog.service.implementation;

import com.rntgroup.testingtask.moviecatalog.api.request.CreateMovieRequest;
import com.rntgroup.testingtask.moviecatalog.api.request.UpdateMovieRequest;
import com.rntgroup.testingtask.moviecatalog.api.response.dto.MovieDto;
import com.rntgroup.testingtask.moviecatalog.domain.mapper.MovieMapper;
import com.rntgroup.testingtask.moviecatalog.domain.repository.DirectorRepository;
import com.rntgroup.testingtask.moviecatalog.domain.repository.MovieRepository;
import com.rntgroup.testingtask.moviecatalog.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

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
    public Collection<MovieDto> listMovies() {
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
        var resource = request.getResource();
        return Optional.ofNullable(resource.getDirectorId())
                .map(directorRepository::findById)
                .map(director -> movieMapper.toResource(resource).setDirector(director))
                .or(() -> Optional.of(movieMapper.toResource(resource)))
                .map(movieRepository::save)
                .map(movieMapper::toDto)
                .orElse(MovieDto.builder().build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public MovieDto update(UpdateMovieRequest request) {
        var resource = request.getResource();
        return Optional.ofNullable(resource.getDirectorId())
                .map(directorRepository::findById)
                .map(director -> movieMapper.toResource(resource).setDirector(director))
                .or(() -> Optional.of(movieMapper.toResource(resource)))
                .map(movieRepository::update)
                .map(movieMapper::toDto)
                .orElse(MovieDto.builder().build());
    }
}
