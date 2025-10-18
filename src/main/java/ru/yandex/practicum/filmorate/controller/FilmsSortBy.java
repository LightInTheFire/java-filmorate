package ru.yandex.practicum.filmorate.controller;

public enum FilmsSortBy {
    YEAR,
    LIKES;

    public static FilmsSortBy fromString(String sortBy) {
        return switch (sortBy.toLowerCase()) {
            case "year" -> YEAR;
            case "likes" -> LIKES;
            default -> null;
        };
    }
}
