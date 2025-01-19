package com.reksoft.moviecatalog.domain.mapper;

import com.reksoft.moviecatalog.api.request.CreateMovieRequest;
import com.reksoft.moviecatalog.api.request.UpdateMovieRequest;
import com.reksoft.moviecatalog.api.response.dto.MovieDto;
import com.reksoft.moviecatalog.domain.model.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MovieMapper {

    /**
     * Converts the given movie to a movie dto.
     *
     * @param source the movie.
     * @return converted movie dto.
     */
    MovieDto toDto(Movie source);

    /**
     * Converts the given movie data to a movie.
     *
     * @param source the movie data
     * @return converted movie.
     */
    Movie toResource(CreateMovieRequest.MovieRequest source);

    /**
     * Converts the given movie data to a movie.
     *
     * @param source the movie data
     * @return converted movie.
     */
    Movie toResource(UpdateMovieRequest.MovieRequest source);
}
