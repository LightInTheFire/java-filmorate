package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"id"})
public class User {
    Long id;
    @NotNull(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    String email;
    @NotBlank(message = "Login must not be empty")
    String login;
    String name;
    @Past(message = "User birthday must be a past date")
    LocalDate birthday;
    Set<Long> friends = new HashSet<>();

    public void addFriend(Long friendId) {
        friends.add(friendId);
    }

    public void removeFriend(Long friendId) {
        friends.remove(friendId);
    }
}
