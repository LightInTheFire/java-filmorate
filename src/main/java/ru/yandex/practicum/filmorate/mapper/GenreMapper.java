package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

@UtilityClass
public class GenreMapper {
    public GenreDto toGenreDto(Genre genre) {
        return new GenreDto(genre.id(), genre.name());
    }
}
