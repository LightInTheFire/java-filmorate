package ru.yandex.practicum.filmorate.dto.user;

import java.time.LocalDate;

public record UserDto(
        long id,
        String email,
        String login,
        String name,
        LocalDate birthday) {
}
