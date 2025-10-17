package ru.yandex.practicum.filmorate.repository.friendship;

import java.util.List;

public interface FriendshipsRepository {
    void addFriendship(long userId, long friendId);

    void removeFriendship(long userId, long friendId);

    boolean isFriends(long userId, long friendId);

    List<Long> findFriendIdsByUserId(long userId);
}