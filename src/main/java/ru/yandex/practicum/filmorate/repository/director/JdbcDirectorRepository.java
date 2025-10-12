package ru.yandex.practicum.filmorate.repository.director;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcDirectorRepository implements DirectorRepository {
    private final NamedParameterJdbcTemplate jdbc;
    private final RowMapper<Director> rowMapper;

    @Override
    public Collection<Director> findAll() {
        String selectAllDirectorsSql = "SELECT * FROM directors ORDER BY director_id";
        return jdbc.query(selectAllDirectorsSql, rowMapper);
    }

    @Override
    public Optional<Director> findById(long id) {
        String selectDirectorByIdSql = "SELECT * FROM directors WHERE director_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        List<Director> directors = jdbc.query(selectDirectorByIdSql, params, rowMapper);

        return directors.isEmpty() ? Optional.empty() : Optional.of(directors.getFirst());
    }

    @Override
    public Director create(Director director) {
        String saveDirectorSql = """
                INSERT INTO directors (name)
                VALUES (:name)""";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", director.getName());

        jdbc.update(saveDirectorSql, params, keyHolder, new String[]{"director_id"});

        director.setId(keyHolder.getKeyAs(Long.class));
        return director;
    }

    @Override
    public void update(Director director) {
        String updateDirectorSql = """
                UPDATE directors
                SET name = :name
                WHERE director_id = :id""";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", director.getName())
                .addValue("id", director.getId());
        jdbc.update(updateDirectorSql, params);
    }

    @Override
    public void deleteById(long id) {
        String deleteDirectorByIdSql = "DELETE FROM directors WHERE director_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        jdbc.update(deleteDirectorByIdSql, params);
    }
}
