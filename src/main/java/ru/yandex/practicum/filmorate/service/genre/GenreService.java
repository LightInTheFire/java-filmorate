package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.dto.genre.GenreDto;

import java.util.Collection;

public interface GenreService {
    Collection<GenreDto> findAll();

    GenreDto findById(long id);
}
