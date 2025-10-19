package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.mparating.MPARatingDto;
import ru.yandex.practicum.filmorate.validation.AfterDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateFilmRequest {
    @NotNull
    Long id;
    String name;
    @Size(message = "Description length must be less than 200", max = 200)
    String description;
    @AfterDate(value = "1895-12-28", message = "Release date must be after December 28, 1895")
    LocalDate releaseDate;
    @Positive(message = "Film duration must be positive")
    Integer duration;
    MPARatingDto mpa;
    List<GenreDto> genres;
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<DirectorDto> directors = new ArrayList<>();

    public boolean hasName() {
        return name != null && !name.isEmpty();
    }

    public boolean hasDescription() {
        return description != null && !description.isEmpty();
    }

    public boolean hasReleaseDate() {
        return releaseDate != null;
    }

    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasGenres() {
        return genres != null;
    }

    public boolean hasMpa() {
        return mpa != null && mpa.id() != null;
    }

    public boolean hasDirectors() {
        return directors != null;
    }
}
