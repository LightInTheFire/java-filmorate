package ru.yandex.practicum.filmorate.storage.mparating;

import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.Storage;

public interface MPARatingStorage extends Storage<MPARating> {
    boolean existsById(long id);
}
