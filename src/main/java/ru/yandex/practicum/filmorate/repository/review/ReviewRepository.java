package ru.yandex.practicum.filmorate.repository.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

public interface ReviewRepository {
    Review save(Review review);

    void update(Review review);

    void delete(long id);

    Optional<Review> findById(long id);

    Collection<Review> findAllByFilm(Long filmId, long count);

    void setLike(long id, long userId);

    void setDislike(long id, long userId);

    void removeLike(long id, long userId);

    void removeDislike(long id, long userId);
}