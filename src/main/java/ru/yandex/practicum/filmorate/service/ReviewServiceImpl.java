package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
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
        existenceCheck(request.getUserId(), User.class);
        existenceCheck(request.getFilmId(), Film.class);
        Review review = ReviewMapper.toReview(request);
        review = reviewRepository.save(review);
        log.info("Review with reviewId {} has been created", review.getId());
        return ReviewMapper.toDto(review);
    }

    @Override
    public ReviewDto update(UpdateReviewRequest request) {
        Review review = existenceCheck(request.getReviewId(), Review.class);
        review = ReviewMapper.updateReviewFields(review, request);
        log.info("Review with reviewId {} has been updated", review.getId());
        reviewRepository.update(review);
        return ReviewMapper.toDto(review);
    }

    @Override
    public void delete(long id) {
        existenceCheck(id, Review.class);
        log.info("Review with reviewId {} has been deleted", id);
        reviewRepository.delete(id);
    }

    @Override
    public ReviewDto findById(long id) {
        return ReviewMapper.toDto(existenceCheck(id, Review.class));
    }

    @Override
    public Collection<ReviewDto> findAllByFilm(Long filmId, long count) {
        existenceCheck(filmId, Film.class);
        return reviewRepository.findAllByFilm(filmId, count).stream()
                .map(ReviewMapper::toDto)
                .toList();
    }

    @Override
    public void setLike(long id, long userId) {
        existenceCheck(id, Review.class);
        existenceCheck(userId, User.class);
        log.info("Like to review {} has been added by user {}", id, userId);
        reviewRepository.setLike(id, userId);
    }

    @Override
    public void setDislike(long id, long userId) {
        existenceCheck(id, Review.class);
        existenceCheck(userId, User.class);
        log.info("Dislike to review {} has been added by user {}", id, userId);
        reviewRepository.setDislike(id, userId);
    }

    @Override
    public void removeLike(long id, long userId) {
        existenceCheck(id, Review.class);
        existenceCheck(userId, User.class);
        log.info("Like to review {} has been removed by user {}", id, userId);
        reviewRepository.removeLike(id, userId);
    }

    @Override
    public void removeDislike(long id, long userId) {
        existenceCheck(id, Review.class);
        existenceCheck(userId, User.class);
        log.info("Dislike to review {} has been removed by user {}", id, userId);
        reviewRepository.removeDislike(id, userId);
    }

    @SuppressWarnings("unchecked")
    private <T> T existenceCheck(long id, Class<T> clazz) {
        if (clazz == Film.class) {
            return (T) filmRepository.findById(id)
                    .orElseThrow(NotFoundException.supplier("Film with filmId %d not found", id));
        } else if (clazz == User.class) {
            return (T) userRepository.findById(id)
                    .orElseThrow(NotFoundException.supplier("User with userId %d not found", id));
        } else if (clazz == Review.class) {
            return (T) reviewRepository.findById(id)
                    .orElseThrow(NotFoundException.supplier("Review with reviewId %d not found", id));
        } else {
            throw new IllegalArgumentException("Unsupported class: " + clazz.getName());
        }
    }
}