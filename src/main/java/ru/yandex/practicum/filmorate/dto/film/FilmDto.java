package ru.yandex.practicum.filmorate.dto.film;

import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.mparating.MPARatingDto;

import java.time.LocalDate;
import java.util.List;

public record FilmDto(
        Long id,
        String name,
        String description,
        LocalDate releaseDate,
        Integer duration,
        MPARatingDto mpa,
        List<GenreDto> genres,
        List<DirectorDto> directors) {
}
