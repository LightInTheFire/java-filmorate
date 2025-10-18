package ru.yandex.practicum.filmorate.service.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.review.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.review.ReviewRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;

    @Override
    public ReviewDto create(NewReviewRequest request) {
        throwIfUserNotFound(request.getUserId());
        throwIfFilmNotFound(request.getFilmId());
        Review review = ReviewMapper.toReview(request);
        review = reviewRepository.save(review);
        log.info("Review with reviewId {} has been created", review.getId());
        return ReviewMapper.toDto(review);
    }

    @Override
    public ReviewDto update(UpdateReviewRequest request) {
        Review review = getReviewOrThrow(request.getReviewId());
        review = ReviewMapper.updateReviewFields(review, request);
        log.info("Review with reviewId {} has been updated", review.getId());
        reviewRepository.update(review);
        return ReviewMapper.toDto(review);
    }

    @Override
    public void delete(long id) {
        getReviewOrThrow(id);
        log.info("Review with reviewId {} has been deleted", id);
        reviewRepository.delete(id);
    }

    @Override
    public ReviewDto findById(long id) {
        return ReviewMapper.toDto(getReviewOrThrow(id));
    }

    @Override
    public Collection<ReviewDto> findAllByFilm(Long filmId, long count) {
        if (filmId != null) {
            throwIfFilmNotFound(filmId);
        }

        return reviewRepository.findAllByFilm(filmId, count).stream()
                .map(ReviewMapper::toDto)
                .toList();
    }

    @Override
    public void setLike(long id, long userId) {
        getReviewOrThrow(id);
        throwIfUserNotFound(userId);
        log.info("Like to review {} has been added by user {}", id, userId);
        reviewRepository.setLike(id, userId);
    }

    @Override
    public void setDislike(long id, long userId) {
        getReviewOrThrow(id);
        throwIfUserNotFound(userId);
        log.info("Dislike to review {} has been added by user {}", id, userId);
        reviewRepository.setDislike(id, userId);
    }

    @Override
    public void removeLike(long id, long userId) {
        getReviewOrThrow(id);
        throwIfUserNotFound(userId);
        log.info("Like to review {} has been removed by user {}", id, userId);
        reviewRepository.removeLike(id, userId);
    }

    @Override
    public void removeDislike(long id, long userId) {
        getReviewOrThrow(id);
        throwIfUserNotFound(userId);
        log.info("Dislike to review {} has been removed by user {}", id, userId);
        reviewRepository.removeDislike(id, userId);
    }

    private Review getReviewOrThrow(long id) {
        return reviewRepository.findById(id)
                .orElseThrow(NotFoundException.supplier("Review with reviewId %d not found", id));
    }

    private void throwIfUserNotFound(long id) {
        userRepository.findById(id)
                .orElseThrow(NotFoundException.supplier("User with userId %d not found", id));
    }

    private void throwIfFilmNotFound(long id) {
        filmRepository.findById(id)
                .orElseThrow(NotFoundException.supplier("Film with filmId %d not found", id));
    }
}
