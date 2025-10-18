package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;

import java.util.Collection;

public interface ReviewService {

    ReviewDto create(NewReviewRequest request);

    ReviewDto update(UpdateReviewRequest request);

    void delete(long id);

    ReviewDto findById(long id);

    Collection<ReviewDto> findAllByFilm(Long filmId, long count);

    void setLike(long id, long userId);

    void setDislike(long id, long userId);

    void removeLike(long id, long userId);

    void removeDislike(long id, long userId);
}