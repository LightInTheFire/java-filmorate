package ru.yandex.practicum.filmorate.controller;

import java.util.Optional;

public enum FilmsSortBy {
    YEAR,
    LIKES;

    public static Optional<FilmsSortBy> fromString(String sortBy) {
        return switch (sortBy.toLowerCase()) {
            case "year" -> Optional.of(YEAR);
            case "likes" -> Optional.of(LIKES);
            default -> Optional.empty();
        };
    }
}
