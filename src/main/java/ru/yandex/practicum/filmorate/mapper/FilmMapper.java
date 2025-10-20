package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

@UtilityClass
public class FilmMapper {
    public FilmDto toFilmDto(Film film) {
        return new FilmDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                MPARatingMapper.toMPARatingDto(film.getMpaRating()),
                film.getGenres()
                        .stream()
                        .map(GenreMapper::toGenreDto)
                        .toList(),
                film.getDirectors()
                        .stream()
                        .map(DirectorMapper::toDirectorDto)
                        .toList());
    }

    public Film toFilm(NewFilmRequest request) {
        return Film.builder()
                .name(request.getName())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .mpaRating(MPARatingMapper.toMPARating(request.getMpa()))
                .genres(request.getGenres()
                        .stream()
                        .map(GenreMapper::toGenre)
                        .distinct()
                        .toList())
                .directors(request.getDirectors()
                        .stream()
                        .map(DirectorMapper::toDirector)
                        .distinct()
                        .toList())
                .build();
    }

    public Film updateFilmFields(Film film, UpdateFilmRequest request) {
        if (request.hasName()) {
            film.setName(request.getName());
        }

        if (request.hasDescription()) {
            film.setDescription(request.getDescription());
        }

        if (request.hasReleaseDate()) {
            film.setReleaseDate(request.getReleaseDate());
        }

        if (request.hasDuration()) {
            film.setDuration(request.getDuration());
        }

        if (request.hasGenres()) {
            film.setGenres(request.getGenres().stream()
                    .map(GenreMapper::toGenre)
                    .distinct()
                    .toList());
        }

        if (request.hasMpa()) {
            film.setMpaRating(MPARatingMapper.toMPARating(request.getMpa()));
        }

        if (request.hasDirectors()) {
            film.setDirectors(request.getDirectors().stream()
                    .map(DirectorMapper::toDirector)
                    .distinct()
                    .toList());
        }

        return film;
    }
}
