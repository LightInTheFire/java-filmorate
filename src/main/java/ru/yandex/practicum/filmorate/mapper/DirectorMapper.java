package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.director.NewDirectorRequest;
import ru.yandex.practicum.filmorate.dto.director.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.model.Director;

@UtilityClass
public class DirectorMapper {

    public DirectorDto toDirectorDto(Director director) {
        return new DirectorDto(director.getId(), director.getName());
    }

    public Director toDirector(NewDirectorRequest directorDto) {
        return Director.builder()
                .name(directorDto.getName())
                .build();
    }

    public Director toDirector(DirectorDto directorDto) {
        return Director.builder()
                .name(directorDto.name())
                .id(directorDto.id())
                .build();
    }

    public Director updateDirectorFields(Director director, UpdateDirectorRequest request) {
        if (request.hasName()) {
            director.setName(request.getName());
        }

        return director;
    }
}
