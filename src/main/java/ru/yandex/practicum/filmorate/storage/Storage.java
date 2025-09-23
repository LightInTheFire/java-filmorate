package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Optional;

public interface Storage<T> {

    Collection<T> findAll();

    Optional<T> findById(long id);

    T create(T t);

    T update(T newT);

    boolean deleteById(long id);

    void throwIfNotExists(long id);
}
