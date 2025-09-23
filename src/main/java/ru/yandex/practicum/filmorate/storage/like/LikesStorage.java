package ru.yandex.practicum.filmorate.storage.like;

public interface LikesStorage {
    void addLike(long userId, long filmId);

    void removeLike(long userId, long filmId);
}
