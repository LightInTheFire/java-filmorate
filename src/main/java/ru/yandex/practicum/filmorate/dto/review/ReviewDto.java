package ru.yandex.practicum.filmorate.dto.review;

public record ReviewDto(
        Long reviewId,
        String content,
        Boolean isPositive,
        Long userId,
        Long filmId,
        Long useful) {
}
