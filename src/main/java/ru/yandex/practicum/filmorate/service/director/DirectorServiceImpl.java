package ru.yandex.practicum.filmorate.service.director;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.director.NewDirectorRequest;
import ru.yandex.practicum.filmorate.dto.director.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.director.DirectorRepository;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {
    private final DirectorRepository directorRepository;

    @Override
    public Collection<DirectorDto> findAll() {
        return directorRepository.findAll()
                .stream()
                .map(DirectorMapper::toDirectorDto)
                .toList();
    }

    @Override
    public DirectorDto findById(long id) {
        return directorRepository.findById(id)
                .map(DirectorMapper::toDirectorDto)
                .orElseThrow(NotFoundException.supplier("Director with id %d not found", id));
    }

    @Override
    public DirectorDto create(NewDirectorRequest request) {
        Director director = DirectorMapper.toDirector(request);
        director = directorRepository.create(director);
        log.info("Director with id {} created", director.getId());
        return DirectorMapper.toDirectorDto(director);
    }

    @Override
    public DirectorDto update(UpdateDirectorRequest request) {
        Director director = directorRepository.findById(request.getId()).orElseThrow(
                NotFoundException.supplier("Director with id %d not found", request.getId())
        );
        director = DirectorMapper.updateDirectorFields(director, request);
        directorRepository.update(director);
        log.info("Director with id {} updated", director.getId());
        return DirectorMapper.toDirectorDto(director);
    }

    @Override
    public void deleteById(long id) {
        directorRepository.deleteById(id);
        log.info("Director with id {} deleted", id);
    }
}
