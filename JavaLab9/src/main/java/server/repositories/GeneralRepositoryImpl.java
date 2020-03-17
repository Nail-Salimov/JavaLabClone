package server.repositories;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import server.models.User;

import java.util.Optional;


public class GeneralRepositoryImpl implements GeneralRepository {

    private final String SQL_SEARCH_CLIENT = "SELECT * FROM clients WHERE id = ? ;";

    private JdbcTemplate template;

    public GeneralRepositoryImpl(JdbcTemplate template) {
        this.template = template;
    }

    private RowMapper<User> userRowMapper = (row, rowNumber) ->
            User.builder()
                    .id(row.getLong("id"))
                    .password(row.getString("password"))
                    .role(row.getString("role"))
                    .build();


    @Override
    public boolean checkClient(Long id, String password) {

           Optional<User> optionalUser = getUser(id);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                String hash = user.getPassword();
                return (BCrypt.checkpw(password, hash));
            } else {
                return false;
            }
    }

    @Override
    public Optional<User> getUser(Long id) {
        try {
            User user = template.queryForObject(SQL_SEARCH_CLIENT, new Object[]{id}, userRowMapper);
            return Optional.ofNullable(user);
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

}
