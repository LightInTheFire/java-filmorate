package ru.yandex.practicum.filmorate.repository.review;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Reaction;
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
                ORDER BY useful_rating DESC
                """), params, mapper);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.getFirst());
    }

    @Override
    public Collection<Review> findAllByFilm(Long filmId, long count) {
        MapSqlParameterSource params = new MapSqlParameterSource("film_id", filmId)
                .addValue("count", count);
        return jdbc.query(BASE_SELECT_REVIEW_SQL.concat("""
                WHERE fr.film_id = COALESCE(:film_id, fr.film_id)
                ORDER BY useful_rating DESC
                LIMIT :count
                """), params, mapper);
    }

    @Override
    public void setReaction(long id, long userId, Reaction reaction) {
        MapSqlParameterSource params = new MapSqlParameterSource("review_id", id)
                .addValue("user_id", userId);
        switch (reaction) {
            case LIKE -> params.addValue("is_positive", true);
            case DISLIKE -> params.addValue("is_positive", false);
        }
        jdbc.update("""
                MERGE INTO review_likes (review_id, user_id, is_positive)
                KEY (review_id, user_id)
                VALUES (:review_id, :user_id, :is_positive)
                """, params);
        updateUsefulRating(params);
    }

    @Override
    public void removeReaction(long id, long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource("review_id", id)
                .addValue("user_id", userId);
        jdbc.update("""
                DELETE FROM review_likes
                WHERE review_id = :review_id AND user_id = :user_id
                """, params);
        updateUsefulRating(params);
    }

    private MapSqlParameterSource getBaseParams(Review review) {
        return new MapSqlParameterSource()
                .addValue("content", review.getContent())
                .addValue("is_Positive", review.getIsPositive());
    }

    private void updateUsefulRating(MapSqlParameterSource params) {
        jdbc.update("""
                UPDATE reviews r
                SET useful_rating = COALESCE((
                        SELECT SUM(CASE WHEN is_positive THEN 1 ELSE -1 END)
                        FROM review_likes rl
                        WHERE rl.review_id = r.review_id),
                    0)
                WHERE r.review_id = :review_id;
                """, params);
    }
}