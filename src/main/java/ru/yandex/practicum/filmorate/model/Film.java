package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.Month;

@Data
public class Film {
    private Long id;
    @NotBlank(message = "Film name should not be null or empty")
    private String name;
    @Size(message = "Description length must be less than 200", max = 200)
    @NotBlank(message = "Description should not be null or empty")
    private String description;
    private LocalDate releaseDate;
    @Positive
    private long duration;

    @AssertTrue(message = "Release date must be after December 28, 1895")
    public boolean isFilmReleaseDateAfter1895() {
        if (releaseDate == null) return true;
        return releaseDate.isAfter(LocalDate.of(1895, Month.DECEMBER, 28));
    }
}
