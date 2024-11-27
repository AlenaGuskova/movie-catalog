package com.rntgroup.testingtask.moviecatalog.domain.mapper;

import com.rntgroup.testingtask.moviecatalog.api.response.dto.Movie;
import com.rntgroup.testingtask.moviecatalog.domain.model.MovieEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MovieEntityMapper {

    /**
     * Converts the movie entity to a dto.
     *
     * @param source the movie entity
     * @return converted movie dto.
     */
    Movie toDto(MovieEntity source);

    /**
     * Converts the movie dto to an entity.
     *
     * @param source the movie dto
     * @return converted movie entity.
     */
    MovieEntity toEntity(Movie source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(MovieEntity source, @MappingTarget MovieEntity target);
}
