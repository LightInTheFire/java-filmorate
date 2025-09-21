package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    @Builder.Default
    Map<Long, FriendshipStatus> friends = new HashMap<>();

    public void addFriend(Long friendId) {
        friends.put(friendId, FriendshipStatus.PENDING);
    }

    public void approveFriend(Long friendId) {
        friends.put(friendId, FriendshipStatus.ACCEPTED);
    }

    public void removeFriend(Long friendId) {
        friends.remove(friendId);
    }
}
