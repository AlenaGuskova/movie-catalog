package com.rntgroup.testingtask.moviecatalog.domain.dao;

import com.rntgroup.testingtask.moviecatalog.domain.model.MovieEntity;

import java.util.List;

public interface MovieDAO extends DAO<MovieEntity> {

    /**
     * Finds list of movies by genre title.
     *
     * @param genreTitle genre title.
     * @return collection of found movies.
     */
    List<MovieEntity> findByGenre(String genreTitle);

    /**
     * Finds list of movies by first name or last name of movie's director or an actor.
     *
     * @param name first name or last name of movie's director or an actor.
     * @return collection of found movies.
     */
    List<MovieEntity> findByName(String name);

    /**
     * Finds list of movies by string in the title.
     *
     * @param prefix string that can be in the movie title.
     * @return collection of found movies.
     */
    List<MovieEntity> findByPrefixInTitle(String prefix);
}
