package ru.yandex.practicum.filmorate.dto.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewReviewRequest {
    @NotBlank(message = "Text must not be null or empty")
    @Size(message = "Text length must be less than 500", max = 500)
    String content;

    @NotNull(message = "Rating must not be null")
    Boolean isPositive;

    @NotNull(message = "User ID must be present")
    Long userId;

    @NotNull(message = "Film ID must be present")
    Long filmId;
}