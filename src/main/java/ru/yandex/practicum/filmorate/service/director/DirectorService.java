package ru.yandex.practicum.filmorate.service.director;

import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.director.NewDirectorRequest;
import ru.yandex.practicum.filmorate.dto.director.UpdateDirectorRequest;

import java.util.Collection;

public interface DirectorService {
    Collection<DirectorDto> findAll();

    DirectorDto findById(long id);

    DirectorDto create(NewDirectorRequest request);

    DirectorDto update(UpdateDirectorRequest request);

    void deleteById(long id);
}
