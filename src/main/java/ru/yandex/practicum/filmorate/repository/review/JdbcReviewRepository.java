package ru.yandex.practicum.filmorate.repository.review;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcReviewRepository implements ReviewRepository {
    private static final String BASE_SELECT_REVIEW_SQL = """
            SELECT r.review_id,
                   r.content,
                   r.is_positive,
                   fr.film_id,
                   fr.user_id,
                   r.useful_rating
            FROM reviews r
            JOIN film_reviews fr ON r.review_id = fr.review_id
            """;
    private static final int UP_UR = 1;
    private static final int DOUBLE_UP_UR = 2;
    private static final int DROP_UR = -1;
    private static final int DOUBLE_DROP_UR = -2;
    private final NamedParameterJdbcOperations jdbc;
    private final RowMapper<Review> mapper;


    @Override
    public Review save(Review review) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = getBaseParams(review);
        jdbc.update("""
                INSERT INTO reviews(content, is_positive)
                VALUES (:content, :is_Positive)
                """, params, keyHolder, new String[]{"review_id"});
        params.addValue("film_id", review.getFilmId());
        params.addValue("user_id", review.getUserId());
        review.setId(keyHolder.getKeyAs(Long.class));
        params.addValue("review_id", review.getId());
        jdbc.update("""
                INSERT INTO film_reviews(film_id, user_id, review_id)
                VALUES (:film_id, :user_id, :review_id)
                """, params);
        review.setUsefulRating(0L);
        return review;
    }

    @Override
    public void update(Review review) {
        MapSqlParameterSource params = getBaseParams(review)
                .addValue("review_id", review.getId());
        jdbc.update("""
                UPDATE reviews
                SET content = :content, is_positive = :is_Positive
                WHERE review_id = :review_id
                """, params);
    }

    @Override
    public void delete(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource("review_id", id);
        jdbc.update("""
                DELETE FROM reviews
                WHERE review_id = :review_id
                """, params);
    }

    @Override
    public Optional<Review> findById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource("review_id", id);
        List<Review> list = jdbc.query(BASE_SELECT_REVIEW_SQL.concat("""
                WHERE r.review_id = :review_id
                """), params, mapper);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.getFirst());
    }

    @Override
    public Collection<Review> findAllByFilm(Long filmId, long count) {
        MapSqlParameterSource params = new MapSqlParameterSource("film_id", filmId)
                .addValue("count", count);
        return jdbc.query(BASE_SELECT_REVIEW_SQL.concat("""
                WHERE fr.film_id = COALESCE(:film_id, fr.film_id)
                LIMIT :count
                """), params, mapper);
    }

    @Override
    public void setLike(long id, long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource("review_id", id)
                .addValue("user_id", userId);
        Boolean isPositiveYet = isPositive(params);
        jdbc.update("""
                MERGE INTO review_likes (review_id, user_id, is_positive)
                KEY (review_id, user_id)
                VALUES (:review_id, :user_id, TRUE)
                """, params);
        if (isPositiveYet == null) {
            changeUsefulRating(params, UP_UR);
        } else if (isPositiveYet) {
            changeUsefulRating(params, DOUBLE_UP_UR);
        }
    }

    @Override
    public void setDislike(long id, long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource("review_id", id)
                .addValue("user_id", userId);
        Boolean isPositiveYet = isPositive(params);
        jdbc.update("""
                MERGE INTO review_likes (review_id, user_id, is_positive)
                KEY (review_id, user_id)
                VALUES (:review_id, :user_id, FALSE)
                """, params);
        if (isPositiveYet == null) {
            changeUsefulRating(params, DROP_UR);
        } else if (isPositiveYet) {
            changeUsefulRating(params, DOUBLE_DROP_UR);
        }
    }

    @Override
    public void removeLike(long id, long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource("review_id", id)
                .addValue("user_id", userId);
        jdbc.update("""
                DELETE FROM review_likes
                WHERE review_id = :review_id AND user_id = :user_id
                """, params);
        changeUsefulRating(params, DROP_UR);
    }

    @Override
    public void removeDislike(long id, long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource("review_id", id)
                .addValue("user_id", userId);
        jdbc.update("""
                DELETE FROM review_likes
                WHERE review_id = :review_id AND user_id = :user_id
                """, params);
        changeUsefulRating(params, UP_UR);
    }

    private MapSqlParameterSource getBaseParams(Review review) {
        return new MapSqlParameterSource()
                .addValue("content", review.getContent())
                .addValue("is_Positive", review.getIsPositive());
    }

    private Boolean isPositive(MapSqlParameterSource params) {
        List<Boolean> list = jdbc.query("""
                SELECT is_positive
                FROM review_likes
                WHERE review_id = :review_id AND user_id = :user_id
                """, params, (rs, rowNum) -> rs.getBoolean("is_positive"));
        return list.isEmpty() ? null : list.getFirst();
    }

    private void changeUsefulRating(MapSqlParameterSource params, int points) {
        params.addValue("points", points);
        jdbc.update("""
                UPDATE reviews
                SET useful_rating = useful_rating + :points
                WHERE review_id = :review_id
                """, params);
    }
}
