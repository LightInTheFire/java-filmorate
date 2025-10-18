package ru.yandex.practicum.filmorate.dto.review;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateReviewRequest {
    @NotNull
    Long reviewId;

    @Size(message = "Text length must be less than 500", max = 500)
    String content;

    Boolean isPositive;

    public boolean hasContent() {
        return this.content != null && !this.content.isEmpty();
    }

    public boolean hasIsPositive() {
        return this.isPositive != null;
    }
}