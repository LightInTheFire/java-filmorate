package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private Long id;
    @NotNull(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    private String email;
    @NotBlank(message = "Login must not be empty")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
