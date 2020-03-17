package server.repositories;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import server.models.client.User;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryJdbcTemplateImpl implements UserRepositoryJdbcTemplate {

    private static final String SQL_SELECT_BY_ID_NOT_CONFIRMED = "SELECT * FROM mm_user where user_id = ?";
    private static final String SQL_SELECT_BY_USERNAME_AND_MAIL_NOT_CONFIRMED = "SELECT * FROM mm_user WHERE username = ? AND email = ? ";
    private static final String SQL_DELETE_USER_NOT_CONFIRMED = "DELETE FROM mm_user WHERE username = ? or email = ?";

    private static final String SQL_SELECT_BY_ID = "SELECT * FROM mm_user where user_id = ? and confirmed = 'true'";
    private static final String SQL_SELECT_BY_UNIQUE_DATA = "SELECT * FROM mm_user WHERE username = ? AND confirmed = 'true'";
    private static final String SQL_SELECT_ALL = "SELECT * FROM mm_user and confirmed = 'true'";
    private static final String SQL_SELECT_BY_USERNAME = "SELECT * FROM mm_user where username = ? and confirmed = 'true'";
    private static final String SQL_SELECT_BY_USERNAME_NOT_CONFIRMED = "SELECT * FROM mm_user where username = ? and confirmed = 'false'";
    private static final String SQL_SELECT_BY_EMAIL = "SELECT * FROM mm_user where email = ? and confirmed = 'true'";

    private static final String SQL_INSERT_USER = "INSERT INTO mm_user(username, password, email) values (?, ?, ?)";

    private static final String SQL_DELETE_USER = "DELETE FROM mm_user WHERE user_id = ?";

    private static final String SQL_CONFIRM_USER = "UPDATE mm_user SET confirmed = 'true' WHERE email = ?";

    private static final String SQL_UPDATE_ADD_TOKEN = "UPDATE mm_user SET token = ? WHERE confirmed = 'false' AND username = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;


    private RowMapper<User> userRowMapper = (row, rowNumber) ->
            User.builder()
                    .id(row.getLong("user_id"))
                    .mail(row.getString("email"))
                    .password(row.getString("password"))
                    .name(row.getString("username"))
                    .token(row.getString("token"))
                    .build();


    @Override
    public Optional<User> find(Long id) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, new Object[]{id}, userRowMapper);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, userRowMapper);
    }

    @Override
    public User save(User entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_USER);
            statement.setString(1, entity.getName());
            statement.setString(2, BCrypt.hashpw(entity.getPassword(), BCrypt.gensalt()));
            statement.setString(3, entity.getMail());
            return statement;
        }, keyHolder);

        entity.setId((Long) keyHolder.getKey());
        return entity;
    }

    @Override
    public void delete(Long aLong) {
        try {
            if (jdbcTemplate.update(SQL_DELETE_USER) == 0) {
                throw new IllegalStateException("no one line update");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void deleteWithNameOrEmail(String name, String email) {
        try {
            jdbcTemplate.update(SQL_DELETE_USER_NOT_CONFIRMED, new Object[]{name, email});
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void addToken(String username, String token) {
        try {
            jdbcTemplate.update(SQL_UPDATE_ADD_TOKEN, token, username);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalStateException("exception with token");
        }
    }

    @Override
    public Optional<User> findByUniqueData(String name, String password) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_SELECT_BY_UNIQUE_DATA, new Object[]{name}, userRowMapper);


            assert user != null;
            if(BCrypt.checkpw(password, user.getPassword())){
                return Optional.of(user);
            }else{
                return Optional.empty();
            }

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByNameAndEmailNotConfirmed(String name, String email) {
        try {
            User user = jdbcTemplate.queryForObject( SQL_SELECT_BY_USERNAME_AND_MAIL_NOT_CONFIRMED, new Object[]{name, email}, userRowMapper);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUsername(String name) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_SELECT_BY_USERNAME, new Object[]{name}, userRowMapper);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUsernameNotConfirmed(String name) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_SELECT_BY_USERNAME_NOT_CONFIRMED, new Object[]{name}, userRowMapper);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_SELECT_BY_EMAIL, new Object[]{email}, userRowMapper);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void confirmUser(String email) {
        try {
            jdbcTemplate.update(SQL_CONFIRM_USER, email);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalStateException("not confirmed");
        }
    }
}
