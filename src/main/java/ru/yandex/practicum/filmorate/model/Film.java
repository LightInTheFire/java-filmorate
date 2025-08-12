package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.Month;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    Long id;
    @NotBlank(message = "Film name should not be null or empty")
    String name;
    @Size(message = "Description length must be less than 200", max = 200)
    @NotBlank(message = "Description should not be null or empty")
    String description;
    LocalDate releaseDate;
    @Positive
    long duration;

    @AssertTrue(message = "Release date must be after December 28, 1895")
    public boolean isFilmReleaseDateAfter1895() {
        if (releaseDate == null) return true;
        return releaseDate.isAfter(LocalDate.of(1895, Month.DECEMBER, 28));
    }
}
