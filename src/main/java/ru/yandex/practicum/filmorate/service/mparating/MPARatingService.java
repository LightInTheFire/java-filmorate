package ru.yandex.practicum.filmorate.service.mparating;

import ru.yandex.practicum.filmorate.dto.mparating.MPARatingDto;

import java.util.Collection;

public interface MPARatingService {
    Collection<MPARatingDto> findAll();

    MPARatingDto findById(long mpaId);
}
