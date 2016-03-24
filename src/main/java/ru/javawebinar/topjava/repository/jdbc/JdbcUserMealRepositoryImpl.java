package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * User: gkislin
 * Date: 26.08.2014
 */

@Repository
public class JdbcUserMealRepositoryImpl implements UserMealRepository {

    private static final BeanPropertyRowMapper<UserMeal> ROW_MAPPER = new BeanPropertyRowMapper<UserMeal>() {
        @Override
        public UserMeal mapRow(ResultSet resultSet, int i) throws SQLException {
            UserMeal userMeal = new UserMeal();
            userMeal.setId(resultSet.getInt("ID"));
            userMeal.setDescription(resultSet.getString("DESCRIPTION"));
            userMeal.setCalories(resultSet.getInt("CALORIES"));
            userMeal.setDateTime(resultSet.getTimestamp("DATE_TIME").toLocalDateTime());

            return userMeal;
        }
    };

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    private SimpleJdbcInsert insertUserMeal;

    @Autowired
    public JdbcUserMealRepositoryImpl(DataSource dataSource)
    {
        this.insertUserMeal = new SimpleJdbcInsert(dataSource)
                .withTableName("MEALS")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public UserMeal save(UserMeal userMeal, int userId)
    {
        MapSqlParameterSource map = new MapSqlParameterSource()
            .addValue("id", userMeal.getId())
            .addValue("description", userMeal.getDescription())
            .addValue("calories", userMeal.getCalories())
            .addValue("date_time", Timestamp.valueOf(userMeal.getDateTime()))
        ;

        if (userMeal.isNew()) {
            map.addValue("user_id", userId);
            Number newKey = insertUserMeal.executeAndReturnKey(map);
            userMeal.setId(newKey.intValue());
        } else {
            namedJdbcTemplate.update("UPDATE meals set description=:description, " +
                    "date_time=:date_time, calories=:calories WHERE id=:id AND user_id:user_id", map);
        }

        return userMeal;
    }

    @Override
    public boolean delete(int id, int userId)
    {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND user_id=?", id, userId) != 0;
    }

    @Override
    public UserMeal get(int id, int userId) {
        List<UserMeal> userMeals =
                jdbcTemplate.query("SELECT * FROM meals WHERE id=? AND user_id=?", ROW_MAPPER, id, userId);
        return DataAccessUtils.singleResult(userMeals);
    }

    @Override
    public List<UserMeal> getAll(int userId)
    {
        List<UserMeal> userMeals = jdbcTemplate.query("SELECT * FROM meals WHERE user_id=? ORDER BY date_time DESC", ROW_MAPPER, userId);
        return userMeals;
    }

    @Override
    public List<UserMeal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId)
    {
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);

        List<UserMeal> userMeals = jdbcTemplate.query("SELECT * FROM meals " +
            "WHERE date_time > ? && date_time < ? AND user_id=? ORDER BY date_time DESC",
            ROW_MAPPER, Timestamp.valueOf(startDate), Timestamp.valueOf(endDate), userId);

        return userMeals;
    }
}
