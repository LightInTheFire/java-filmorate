package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.mparating.MPARatingDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MPARatingMapper;
import ru.yandex.practicum.filmorate.storage.mparating.MPARatingStorage;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class MPARatingService {
    private final MPARatingStorage mpaStorage;

    public Collection<MPARatingDto> findAll() {
        return mpaStorage.findAll()
                .stream()
                .map(MPARatingMapper::toMPARatingDto)
                .toList();
    }

    public MPARatingDto findById(long mpaId) {
        return mpaStorage.findById(mpaId)
                .map(MPARatingMapper::toMPARatingDto)
                .orElseThrow(NotFoundException.supplier("MPA Rating with id %d not found", mpaId));
    }
}
