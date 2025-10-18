package ru.yandex.practicum.filmorate.dto.review;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewDto {
    Long reviewId;
    String content;
    Boolean isPositive;
    Long userId;
    Long filmId;
    Long useful;
}

