package server.repositories;

import context.Component;
import server.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import java.util.Properties;

public class UserRepositoryImpl implements UserRepository, Component {

    private Connection connection;
    private final String SEARCH_CLIENT = "SELECT * FROM store_clients WHERE name = ?;";
    private final String ADD_CLIENT = "INSERT INTO store_clients(name, password) VALUES (?, ?)";
    private final String SEARCH_CLIENT_BY_ID = "SELECT * FROM store_clients WHERE id = ?;";


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<User> findClient(String name, String password) {
        try {
            User user = null;
            PreparedStatement statement = connection.prepareStatement(SEARCH_CLIENT);
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String userName = resultSet.getString("name");
                long userId = resultSet.getLong("id");
                String userPassword = resultSet.getString("password");

                user = new User(userId, userName);
                if (!BCrypt.checkpw(password, userPassword)) {
                    user = null;
                }
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<User> findClientById(int id) {
        try {
            User user = null;
            PreparedStatement statement = connection.prepareStatement(SEARCH_CLIENT_BY_ID);
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String userName = resultSet.getString("name");
                long userId = resultSet.getLong("id");

                user = new User(userId, userName);
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<User> addClient(String name, String password) {

        if (!clientIsExist(name)) {
            addNewClient(name, password);
            return findClient(name, password);
        } else {
            return Optional.empty();
        }
    }

    public boolean clientIsExist(String name){
        try {
            PreparedStatement statement = connection.prepareStatement(SEARCH_CLIENT);
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
    private void addNewClient(String name, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement(ADD_CLIENT);
            statement.setString(1, name);
            statement.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException();
            } else {
                statement.close();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
