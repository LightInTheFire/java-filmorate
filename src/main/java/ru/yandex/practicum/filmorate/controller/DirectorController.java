package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.director.NewDirectorRequest;
import ru.yandex.practicum.filmorate.dto.director.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.service.director.DirectorService;

import java.util.Collection;

@RestController
@RequestMapping("/directors")
@Slf4j
@RequiredArgsConstructor
@Validated
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public Collection<DirectorDto> findAll() {
        log.trace("Collection of all directors requested");
        return directorService.findAll();
    }

    @GetMapping("/{id}")
    public DirectorDto findById(@PathVariable @Positive long id) {
        log.trace("Find director by id requested, id: {}", id);
        return directorService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DirectorDto create(@RequestBody @Valid NewDirectorRequest request) {
        log.trace("Create new director requested: {}", request);
        return directorService.create(request);
    }

    @PutMapping
    public DirectorDto update(@RequestBody @Valid UpdateDirectorRequest request) {
        log.trace("Update director requested: {}", request);
        return directorService.update(request);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable @Positive long id) {
        log.trace("Delete director by id requested, id: {}", id);
        directorService.deleteById(id);
    }
}
