package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Collection<GenreDto> findAll() {
        return genreStorage.findAll()
                .stream()
                .map(GenreMapper::toGenreDto)
                .toList();
    }

    public GenreDto findById(long id) {
        return GenreMapper.toGenreDto(
                genreStorage.findById(id).orElseThrow(
                        NotFoundException.supplier("Genre with id %d not foud", id)
                )
        );
    }
}
