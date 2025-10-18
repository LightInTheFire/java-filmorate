package ru.yandex.practicum.filmorate.repository.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DirectorRepository {
    Collection<Director> findAll();

    Optional<Director> findById(long id);

    Director create(Director director);

    void update(Director director);

    void deleteById(long id);

    List<Director> getByIds(List<Long> directorIds);
}
