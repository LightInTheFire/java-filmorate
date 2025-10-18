package ru.yandex.practicum.filmorate.service.recommendation;

import ru.yandex.practicum.filmorate.dto.film.FilmDto;

import java.util.Collection;

public interface RecommendationService {
    Collection<FilmDto> findFilmRecommendations(long userId);
}
