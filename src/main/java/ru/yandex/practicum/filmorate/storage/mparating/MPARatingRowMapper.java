package ru.yandex.practicum.filmorate.storage.mparating;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MPARatingRowMapper implements RowMapper<MPARating> {
    @Override
    public MPARating mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MPARating(
                rs.getLong("mpa_id"),
                rs.getString("name")
        );
    }
}
