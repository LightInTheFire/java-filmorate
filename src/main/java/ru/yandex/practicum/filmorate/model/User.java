package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Long id;
    @NotNull(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    String email;
    @NotBlank(message = "Login must not be empty")
    String login;
    String name;
    @Past
    LocalDate birthday;
}
