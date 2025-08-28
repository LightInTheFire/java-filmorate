package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Long id;
    @NotNull(message = "Email must not be empty", groups = Marker.OnCreate.class)
    @Email(message = "Email must be valid",
            groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    String email;
    @NotBlank(message = "Login must not be empty",
            groups = Marker.OnCreate.class)
    String login;
    String name;
    @Past(message = "User birthday must be a past date",
            groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    LocalDate birthday;
    Set<Long> friends = new HashSet<>();

    public void addFriend(Long friendId) {
        friends.add(friendId);
    }

    public void removeFriend(Long friendId) {
        friends.remove(friendId);
    }
}
