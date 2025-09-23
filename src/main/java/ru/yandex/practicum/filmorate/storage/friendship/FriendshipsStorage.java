package ru.yandex.practicum.filmorate.storage.friendship;

public interface FriendshipsStorage {
    void addFriendship(long userId, long friendId);

    void removeFriendship(long userId, long friendId);
}
