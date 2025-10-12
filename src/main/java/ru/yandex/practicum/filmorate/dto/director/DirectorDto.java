package ru.yandex.practicum.filmorate.dto.director;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DirectorDto(Long id, String name) {
}
